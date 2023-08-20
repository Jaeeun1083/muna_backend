package com.life.muna.product.service;

import com.life.muna.common.dto.MaxProductInfoResponse;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.like.mapper.ProductLikeMapper;
import com.life.muna.location.domain.Location;
import com.life.muna.location.mapper.LocationMapper;
import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.ProductDetail;
import com.life.muna.product.domain.enums.LocationRange;
import com.life.muna.product.domain.enums.ProductStatus;
import com.life.muna.product.dto.create.ProductCreateRequest;
import com.life.muna.product.dto.detail.MyInfoOfProductResponse;
import com.life.muna.product.dto.detail.ProductDetailResponse;
import com.life.muna.product.dto.list.ProductListRequest;
import com.life.muna.product.dto.list.ProductListResponse;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.productReq.mapper.ReqProductMapper;
import com.life.muna.user.domain.User;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.life.muna.common.error.ErrorCode.NOT_FOUND_USER;
import static com.life.muna.common.util.ImageUtil.resizeThumbnail;
import static com.life.muna.product.dto.create.ProductCreateRequest.toProduct;
import static com.life.muna.product.dto.create.ProductCreateRequest.toProductDetail;

@Service
public class ProductService {
    private final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final String defaultPath;
    private final static int PAGE_SIZE = 30;

    private final static long POSSIBLE_LIFT_UP_SECOND = 259200;

    private final ProductMapper productMapper;
    private final ReqProductMapper reqProductMapper;
    private final ProductLikeMapper productLikeMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    public ProductService(ProductMapper productMapper, ReqProductMapper reqProductMapper, ProductLikeMapper productLikeMapper, UserMapper userMapper, LocationMapper locationMapper, @Value("${image.upload-path}") String defaultPath) {
        this.productMapper = productMapper;
        this.reqProductMapper = reqProductMapper;
        this.productLikeMapper = productLikeMapper;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this. defaultPath = defaultPath;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createProduct(String emailFromToken, ProductCreateRequest productCreateRequest, MultipartFile thumbnail, List<MultipartFile> images) {
        User user =  userMapper.findByEmail(emailFromToken).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if (user.getLocationDongCd() == null) throw new BusinessException(ErrorCode.UNPROCESSABLE_USER_LOCATION);

        if (thumbnail.isEmpty()) throw new BusinessException(ErrorCode.NO_IMAGES_TO_UPLOAD);

        long userCode = user.getUserCode();
        LocalDateTime now = LocalDateTime.now();

        byte[] resizedThumbnail = resizeThumbnail(thumbnail);
        Product product = toProduct(productCreateRequest, resizedThumbnail,  userCode, user.getLocationDongCd(), now);
        int rowCount = productMapper.saveProduct(product);

        long productCode = product.getProductCode();
        if (rowCount<=0 || productCode <= 0) throw new BusinessException(ErrorCode.FAILED_TO_CREATE_PRODUCT);

        uploadImage(images, userCode, productCode);
        int imageCount = images.size();
        String imageLink = String.format("/upload/U%07d/P%07d/",userCode, productCode);

        ProductDetail productDetail = toProductDetail(productCreateRequest, userCode, productCode, imageLink, imageCount, now);
        int productDetailInsert = productMapper.saveProductDetail(productDetail);
        if (productDetailInsert <= 0) throw new BusinessException(ErrorCode.FAILED_TO_CREATE_PRODUCT);
        return productCode;
    }

    private void uploadImage(List<MultipartFile> images, long userCode, long productCode) {
        String folderPath = String.format("%s/U%07d/P%07d/", defaultPath, userCode, productCode);
        File file = new File(folderPath);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File value : files) {
                        if (value.delete()) {
                            LOG.info(value.getName() + " 삭제 성공");
                        } else {
                            LOG.info(value.getName() + " 삭제 실패");
                        }
                    }
                }
            }
            if (!file.delete()) throw new BusinessException(ErrorCode.FAILED_TO_DELETE_DIRECTORY);
        }
        if (!file.mkdirs()) throw new BusinessException(ErrorCode.FAILED_TO_CREATE_DIRECTORY);

        if (!images.isEmpty()) {
            for(int i = 0; i < images.size() ; i++) {
                String filePath =  String.format("%s%d.jpg", folderPath, i);
                try {
                    images.get(i).transferTo(new File(filePath));
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.FAILED_TO_SAVE_IMAGE);
                }
            }
        }
    }

    public List<ProductListResponse> getProductList(String emailFromToken, ProductListRequest productListRequest) {
        User user =  userMapper.findByEmail(emailFromToken).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
        if(user.getLocationDongCd() == null) throw new BusinessException(ErrorCode.UNPROCESSABLE_USER_LOCATION);

        List<ProductListResponse> productList;
        if (!productListRequest.getLocationRange().equals(LocationRange.L000.getCode())) {
            setLocationCodeByRange(user.getLocationDongCd(), productListRequest);
        }


        productListRequest.setPageInfo(PAGE_SIZE);
        productList = productMapper.findProductList(productListRequest);
        return productList;
    }

    public ProductDetailResponse getProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();

        Optional<ProductDetailResponse> productDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if (productDetailOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT);
        ProductDetailResponse productDetailResponse = productDetailOptional.get();

        Long sellerCode = productDetailResponse.getUserCode();
        Optional<User> sellerOptional = userMapper.findByUserCode(sellerCode);
        if (sellerOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_SELLER);

        boolean isMe = sellerCode.equals(userCode);
        if (!isMe) productMapper.updateViewCnt(productCode, productDetailResponse.getViews() + 1);

        productDetailResponse.setSellerData(sellerOptional.get(), isMe);
        return productDetailResponse;
    }

    public MyInfoOfProductResponse getMyInfoOfProductResponse(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();

        boolean isRequested = reqProductMapper.existsByUserCodeAndProductCode(userCode, productCode);
        boolean isLiked = productLikeMapper.existsByUserCodeAndProductCode(userCode, productCode);
        return MyInfoOfProductResponse.setMyInformation(isRequested, isLiked);
    }

    public MaxProductInfoResponse getMaxProductInfo(ProductListRequest productListRequest) {
        if (productListRequest.getMaxProductCode() == null || productListRequest.getMaxProductCode() == 0) {
            return productMapper.findMaxProductInfo(PAGE_SIZE);
        }
        return new MaxProductInfoResponse(0, 0L);
    }

    public long liftUpProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();

        Product product = productMapper.findProductByProductCode(productCode).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        if (!product.getUserCode().equals(userCode)) throw new BusinessException(ErrorCode.NO_AUTHENTICATION_PRODUCT);
        if (!product.getProductStatus().equals(ProductStatus.AVL)) throw new BusinessException(ErrorCode.CANNOT_MODIFY_PRODUCT);

        LocalDateTime today = LocalDateTime.now(); // 오늘
        Duration duration = Duration.between(product.getUpdateDate(), today);
        long secondsDifference = Math.abs(duration.getSeconds());

        if (secondsDifference < POSSIBLE_LIFT_UP_SECOND) {
            return POSSIBLE_LIFT_UP_SECOND - secondsDifference;
        }

        productMapper.updateDate(productCode);
        return 0L;
    }

    @Transactional
    public boolean withDrawProduct(String emailFromToken, Long productCode) {
        // TODO 구현 예정
        return false;
    }

    private void setLocationCodeByRange(Long userLocationDongCd, ProductListRequest productListRequest) {
        Location location = locationMapper.findByLocationDongCd(userLocationDongCd);
        // locationRange 를 받아오는 부분(L001/L002)에서 시, 구가 합쳐져있는 애들은 L001(시/도)로 검색되도록 처리
        // 구/군을 제외한 67개의 LocationGuNm 값을 배열(리스트)로 받아와서
        List<String> notLikeSiList = locationMapper.findNotEndingWithGuOrGun();

        // LocationGuNm이 위에 67개의 포함될 경우 L001로 바꿔주면 됨
        if(notLikeSiList.contains(location.getLocationGuNm())){
            productListRequest.setLocationRange(LocationRange.L001.getCode());
        }

        LocationRange locationRange = LocationRange.fromCode(productListRequest.getLocationRange());

        Long locationCode = calculateLocationCode(locationRange, location);
        productListRequest.setLocationCode(locationCode);
    }


    private Long calculateLocationCode(LocationRange locationRange, Location location) {
        return switch (locationRange) {
            case L001 -> subStringValue(0, 2, location.getLocationDongCd());
            case L002 -> subStringValue(0, 5, location.getLocationDongCd());
            case L003 -> location.getLocationDongCd();
            default -> null;
        };
    }

    private Long subStringValue(int beginIndex, int endIndex, Long value) {
        String strValue = Long.toString(value);
        return Long.parseLong(strValue.substring(beginIndex, endIndex));
    }
}
