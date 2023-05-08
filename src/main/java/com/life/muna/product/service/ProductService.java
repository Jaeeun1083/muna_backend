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
import com.life.muna.product.dto.*;
import com.life.muna.product.mapper.ProductHistoryMapper;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.product.mapper.ReqProductMapper;
import com.life.muna.user.domain.User;
import com.life.muna.product.dto.ReqUserProfile;
import com.life.muna.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.life.muna.common.util.TimeConverter.convert;
import static com.life.muna.location.service.LocationService.getLocationName;
import static com.life.muna.product.dto.ProductCreateRequest.toProduct;
import static com.life.muna.product.dto.ProductCreateRequest.toProductDetail;

@Service
public class ProductService {
    private final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final String defaultPath;
    private final static int PAGE_SIZE = 30;

    private final ProductMapper productMapper;
    private final ReqProductMapper reqProductMapper;
    private final ProductLikeMapper productLikeMapper;
    private ProductHistoryMapper productHistoryMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    public ProductService(ProductMapper productMapper, ReqProductMapper reqProductMapper, ProductLikeMapper productLikeMapper, ProductHistoryMapper productHistoryMapper, UserMapper userMapper, LocationMapper locationMapper, @Value("${image.upload-path}") String defaultPath) {
        this.productMapper = productMapper;
        this.reqProductMapper = reqProductMapper;
        this.productLikeMapper = productLikeMapper;
        this.productHistoryMapper = productHistoryMapper;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this. defaultPath = defaultPath;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean createProduct(String emailFromToken, ProductCreateRequest productCreateRequest, List<MultipartFile> images) {
        User user =  userMapper.findUserByEmail(emailFromToken).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE));
        if (user.getLocationDongCd() == null) throw new BusinessException(ErrorCode.UNPROCESSABLE_USER_LOCATION);
        long userCode = user.getUserCode();
        Date now = new Date();

        Product product = toProduct(productCreateRequest, userCode, user.getLocationDongCd(), now);
        int rowCount = productMapper.save(product);
        long productCode = product.getProductCode();
        if (rowCount<=0 || productCode <= 0) throw new BusinessException(ErrorCode.FAILED_TO_CREATE_PRODUCT);

        String imageLink = String.format("/upload/U%07d/P%07d/",userCode, productCode);

        int imageCount = images.size();
        if (imageCount == 0) throw new BusinessException(ErrorCode.NO_IMAGES_TO_UPLOAD);

        String folderPath = String.format("%s/U%07d/P%07d/",defaultPath, userCode, productCode);
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) throw new BusinessException(ErrorCode.FAILED_TO_CREATE_DIRECTORY);
        }

        for (int i = 0; i < imageCount; i++) {
            MultipartFile image = images.get(i);
            String imagePath = String.format("%s%d.jpg", folderPath, i);
            try {
                image.transferTo(new File(imagePath));
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.FAILED_TO_SAVE_IMAGE);
            }
        }

        ProductDetail productDetail = toProductDetail(productCreateRequest, userCode, productCode, imageLink, imageCount, now);
        int productDetailInsert = productMapper.saveProductDetail(productDetail);
        if (productDetailInsert <= 0) throw new BusinessException(ErrorCode.FAILED_TO_CREATE_PRODUCT);
        return true;
    }

    public List<ProductListResponse> getProductList(String emailFromToken, ProductListRequest productListRequest) {
        User user =  userMapper.findUserByEmail(emailFromToken).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_BY_USER_CODE));
        if(user.getLocationDongCd() == null) throw new BusinessException(ErrorCode.UNPROCESSABLE_USER_LOCATION);
        Location location = locationMapper.findByLocationDongCd(user.getLocationDongCd());
        List<ProductListResponse> productList;
        LocationRange locationRange = LocationRange.fromCode(productListRequest.getLocationRange());

        Long locationCode = switch (locationRange) {
            case L001 -> subStringValue(0, 2, location.getLocationDongCd());
            case L002 -> subStringValue(0, 5, location.getLocationDongCd());
            case L003 -> location.getLocationDongCd();
            default -> null;
        };
        productListRequest.setLocationCode(locationCode);
        productListRequest.setPageInfo(PAGE_SIZE);
        productList = productMapper.findProductList(productListRequest);
        return productList;
    }

    public ProductDetailResponse getProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);

        Optional<ProductDetailResponse> productDetailOptional = productMapper.findProductDetailByProductCode(productCode);
        if (productDetailOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL);
        ProductDetailResponse productDetailResponse = productDetailOptional.get();
        Long sellerCode = productDetailResponse.getUserCode();
        Optional<User> sellerOptional = userMapper.findUserByUserCode(sellerCode);
        if (sellerOptional.isEmpty()) throw new BusinessException(ErrorCode.NOT_FOUND_SELLER);

        boolean isMe = sellerCode.equals(userCode);
        if (!isMe) productMapper.updateViewCnt(productCode, productDetailResponse.getViews() + 1);
        return productDetailResponse.setSellerData(sellerOptional.get(), isMe);
    }

    public MyInfoOfProductResponse getMyInfoOfProductResponse(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);

        boolean isRequested = reqProductMapper.existsByUserCodeAndProductCode(userCode, productCode);
        boolean isLiked = productLikeMapper.existsByUserCodeAndProductCode(userCode, productCode);
        return MyInfoOfProductResponse.setMyInformation(isRequested, isLiked);
    }

    public List<ProductHistoryResponse> getReceivedProductList(String emailFromToken) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);

        List<Product> productList = productHistoryMapper.findReceivedProductByUserCode(userCode);
        List<ProductHistoryResponse> productHistoryResponses = new ArrayList<>();
        for (Product product : productList) {
            productHistoryResponses.add(ProductHistoryResponse.of(product));
        }
        return productHistoryResponses;
    }

//    public List<ProductRegiListResponse> getRegisteredProductList(Long userCode, int page) {
    public List<ProductHistoryResponse> getRegisteredProductList(String emailFromToken) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);

//        int offset = (Math.max(page - 1, 0)) * PAGE_SIZE;
        List<Product> productList = productMapper.findProductByUserCode(userCode);
        List<ProductHistoryResponse> productHistoryResponse = new ArrayList<>();
        for (Product product : productList) {
            int reqCnt = reqProductMapper.findChatReqCountByProductCode(product.getProductCode());
            ProductHistoryResponse response = ProductHistoryResponse.of(product, reqCnt);
            productHistoryResponse.add(response);
        }
        return productHistoryResponse;
    }

    public ReqReceivedResponse getReceivedReqOfProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findUserCodeByEmail(emailFromToken);

        Product product = productMapper.findProductByProductCode(productCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_DETAIL));
        if (!product.getUserCode().equals(userCode)) throw new BusinessException(ErrorCode.CANNOT_SEARCH_OTHER_PRODUCT);

        int reqCnt = reqProductMapper.findChatReqCountByProductCode(productCode);
        List<ReqUserProfile> reqUserProfileList = new ArrayList<>();

        reqProductMapper.findChatReqByProductCode(productCode)
                .forEach(reqProduct -> {
                    userMapper.findUserByUserCode(reqProduct.getUserCode())
                            .ifPresent(user -> {
                                Location location = locationMapper.findByLocationDongCd(user.getLocationDongCd());
                                String locationName = getLocationName(location);

                                reqUserProfileList.add(ReqUserProfile.builder()
                                        .userNickname(user.getNickname())
                                        .userProfileImage(user.getProfileImage())
                                        .location(locationName)
                                        .reqContent(reqProduct.getReqContent())
                                        .chatStatus(reqProduct.getChatStatus())
                                        .insertDate(convert(reqProduct.getInsertDate()))
                                        .updateDate(convert(reqProduct.getUpdateDate()))
                                        .build());
                            });
                });

        return ReqReceivedResponse.builder()
                .product(ProductHistoryResponse.of(product, reqCnt))
                .reqUserProfileList(reqUserProfileList)
                .build();
    }

    public MaxProductInfoResponse getMaxProductInfo(ProductListRequest productListRequest) {
        if (productListRequest.getMaxProductCode() == null || productListRequest.getMaxProductCode() == 0) {
            return productMapper.findMaxProductInfo(PAGE_SIZE);
        }
        return new MaxProductInfoResponse(0, 0L);
    }

    public MaxProductInfoResponse getMaxRegisteredProductInfo(Long userCode) {
        return productMapper.findMaxRegisteredProductInfo(userCode, PAGE_SIZE);
    }

    private Long subStringValue(int beginIndex, int endIndex, Long value) {
        String strValue = Long.toString(value);
        return Long.parseLong(strValue.substring(beginIndex, endIndex));
    }
}
