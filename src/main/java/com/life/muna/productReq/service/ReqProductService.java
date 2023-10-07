package com.life.muna.productReq.service;

import com.life.muna.chat.domain.ChatRoom;
import com.life.muna.chat.domain.enums.ChatStatus;
import com.life.muna.chat.mapper.ChatRoomMapper;
import com.life.muna.common.error.ErrorCode;
import com.life.muna.common.error.exception.BusinessException;
import com.life.muna.location.domain.Location;
import com.life.muna.location.mapper.LocationMapper;
import com.life.muna.product.domain.Product;
import com.life.muna.product.domain.enums.ProductStatus;
import com.life.muna.product.dto.ReqProductReceivedListResponse;
import com.life.muna.product.dto.request.ProductShareRequest;
import com.life.muna.product.mapper.ProductMapper;
import com.life.muna.productReq.domain.ReqProduct;
import com.life.muna.productReq.domain.enums.ReqStatus;
import com.life.muna.productReq.dto.ReqProductRequestedListResponse;
import com.life.muna.productReq.dto.list.ReqProductReceivedReqListResponse;
import com.life.muna.productReq.dto.list.ReqReceivedResponse;
import com.life.muna.productReq.dto.profile.ReqUserProfile;
import com.life.muna.productReq.mapper.ReqProductMapper;
import com.life.muna.user.domain.User;
import com.life.muna.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.life.muna.common.error.ErrorCode.NOT_FOUND_USER;
import static com.life.muna.location.service.LocationService.getLocationName;

@Service
public class ReqProductService {
    private final ProductMapper productMapper;
    private final ReqProductMapper reqProductMapper;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;
    private final ChatRoomMapper chatRoomMapper;

    public ReqProductService(ProductMapper productMapper, ReqProductMapper reqProductMapper, UserMapper userMapper, LocationMapper locationMapper, ChatRoomMapper chatRoomMapper) {
        this.productMapper = productMapper;
        this.reqProductMapper = reqProductMapper;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
        this.chatRoomMapper = chatRoomMapper;
    }

    public int getMyRequestCount(String emailFromToken) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
        return findUser.getReqCnt() + findUser.getCashedReqCnt();
    }

    public boolean requestShareProduct(String emailFromToken, ProductShareRequest productShareRequest) {

        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();
        Long productCode = productShareRequest.getProductCode();

        Product findProduct = productMapper.findProductByProductCode(productCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        if (!findProduct.getProductStatus().equals(ProductStatus.AVL)) throw new BusinessException(ErrorCode.DISABLED_PRODUCT_REQUEST);

        if(findProduct.getUserCode().equals(userCode)) throw new BusinessException(ErrorCode.CANNOT_REQUEST_MY_PRODUCT);

        boolean isRequested = reqProductMapper.existsByRequesterCodeAndProductCode(userCode, productCode);
        if (isRequested) throw new BusinessException(ErrorCode.ALREADY_PRODUCT_REQUEST);

        Optional<User> userOptional = userMapper.findByUserCode(userCode);
        User findUser = userOptional.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        int possibleReqCnt = findUser.getReqCnt();
        int possibleCashedReqCnt = findUser.getCashedReqCnt();
        if (possibleReqCnt  + possibleCashedReqCnt <= 0)  throw new BusinessException(ErrorCode.EXCEED_PRODUCT_REQUEST_COUNT);

        int updateReqCntResult = productMapper.updateReqCnt(findProduct.getProductCode(), findProduct.getReqCnt() + 1);
        if (updateReqCntResult == 1) {
            if (possibleReqCnt > 0) {
                ReqProduct reqProduct = productShareRequest.toEntity(productShareRequest, userCode, findProduct.getUserCode(), false);
                int requestChatResult = reqProductMapper.save(reqProduct);
                userMapper.saveReqCnt(userCode, possibleReqCnt - 1, possibleCashedReqCnt);
            } else {
                ReqProduct reqProduct = productShareRequest.toEntity(productShareRequest, userCode, findProduct.getUserCode(), true);
                int requestChatResult = reqProductMapper.save(reqProduct);
                userMapper.saveReqCnt(userCode, possibleReqCnt, possibleCashedReqCnt -1);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean withdrawRequestProduct(String emailFromToken, Long productCode) {
        User findUser = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
        Long userCode = findUser.getUserCode();
        ReqProduct reqProduct = reqProductMapper.findByRequesterCodeAndProductCode(userCode, productCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT_REQ));

        ReqStatus reqStatus = reqProduct.getReqStatus();

        Optional<ChatRoom> chatRoomOptional = chatRoomMapper
                .findByProductReqCode(reqProduct.getProductReqCode())
                .filter(chatRoom -> !chatRoom.getChatStatus().equals(ChatStatus.R_DEL)
                        && !chatRoom.getChatStatus().equals(ChatStatus.P_DEL)
                        && !chatRoom.getChatStatus().equals(ChatStatus.B_DEL));

        if (reqStatus.equals(ReqStatus.CHAT)) {
            // 채팅방이 이미 생성되어 있을 경우 판매자가 나눔 확정을 눌렀을 경우 취소가 안됨. 그렇지 않을 경우 ChatStatus DEL (=> 채팅 불가능 하도록 함
            if (chatRoomOptional.isPresent()) {
                ChatRoom chatRoom = chatRoomOptional.get();

                if (chatRoom.getRequesterCode().equals(userCode)) {
                    if (chatRoom.getChatStatus() == ChatStatus.CONF) {
                        return false;
                    } else if (chatRoom.getChatStatus() == ChatStatus.NCONF) {
                        chatRoomMapper.saveChatStatus(reqProduct.getProductReqCode(), false, false, ChatStatus.R_DEL);
                        handleWithdrawRequest(findUser, reqProduct);
                    }
                }
            }
        } else if (reqStatus.equals(ReqStatus.DONE)) {
            reqProductMapper.deleteByRequesterCodeAndProductCode(userCode, productCode);
        } else if (reqStatus.equals(ReqStatus.REQ) || reqStatus.equals(ReqStatus.REJ)) {
            handleWithdrawRequest(findUser, reqProduct);
        }

        Optional<Product> findProductOptional = productMapper.findProductByProductCode(productCode);
        findProductOptional.ifPresent(product -> productMapper.updateReqCnt(product.getProductCode(), product.getReqCnt() > 0 ? product.getReqCnt() - 1 : product.getReqCnt()));
        // ProductShareRequest(상품코드, 전송메세지), 받는사람코드(나눔이니까->판매자), 내닉네임, 현재시간

        return true;
    }

    private void handleWithdrawRequest(User requester, ReqProduct reqProduct) {
        reqProductMapper.deleteByRequesterCodeAndProductCode(requester.getUserCode(), reqProduct.getProductCode());
        int possibleReqCnt = requester.getReqCnt();
        int possibleCashedReqCnt = requester.getCashedReqCnt();
        if (reqProduct.isCashedReq()) {
            userMapper.saveReqCnt(reqProduct.getRequesterCode(), possibleReqCnt, possibleCashedReqCnt + 1);
        } else {
            userMapper.saveReqCnt(reqProduct.getRequesterCode(), possibleReqCnt + 1, possibleCashedReqCnt);
        }
        chatRoomMapper.saveChatStatus(reqProduct.getProductReqCode(), false, false, ChatStatus.R_DEL);
    }

    public List<ReqProductRequestedListResponse> getRequestProductList(String emailFromToken) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();
        List<ReqProduct> ReqProducts = reqProductMapper.findByRequesterCode(userCode)
                .stream()
                .filter(reqProduct -> !reqProduct.getReqStatus().equals(ReqStatus.DONE))
                .toList();
        List<ReqProductRequestedListResponse> productListResponses = new ArrayList<>();

        for (ReqProduct reqProduct : ReqProducts) {
            Optional<Product> findProductOptional = productMapper.findProductByProductCode(reqProduct.getProductCode());

            if (findProductOptional.isPresent()) {
                    // findProduct가 null이 아닌 경우에만 실행되는 코드
                Product product = findProductOptional.get();
                ReqProductRequestedListResponse response = ReqProductRequestedListResponse.of(product, reqProduct.getReqStatus());
                productListResponses.add(response);

            }
        }
        return productListResponses;
    }

    public List<ReqProductReceivedListResponse> getReceivedProductList(String emailFromToken) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();
        List<Product> productList = productMapper.findProductByUserCode(userCode);
        List<ReqProductReceivedListResponse> responses = new ArrayList<>();

        for (Product product : productList) {
            if (!product.getProductStatus().equals(ProductStatus.DEL) && !product.getProductStatus().equals(ProductStatus.COMP)) {
                List<ReqProduct> reqProductList = reqProductMapper.findByProductCode(product.getProductCode());

                if (product.getReqCnt() > 0 && !reqProductList.isEmpty()) {
                    boolean isRead = reqProductList.stream()
                            .anyMatch(reqProduct -> reqProduct.getReqRead() != null && reqProduct.getReqRead());

                    ReqProductReceivedListResponse response = ReqProductReceivedListResponse.of(product, reqProductList.size(), isRead);
                    responses.add(response);
                }
            }
        }
        return responses;
    }

    public ReqReceivedResponse getReceivedReqOfProduct(String emailFromToken, Long productCode) {
        Long userCode = userMapper.findByEmail(emailFromToken)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER)).getUserCode();
        ReqReceivedResponse reqReceived = new ReqReceivedResponse();

        Product findProduct = productMapper.findProductByProductCode(productCode)
                .filter(product -> !product.getProductStatus().equals(ProductStatus.DEL) && !product.getProductStatus().equals(ProductStatus.COMP))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));
        if (!findProduct.getUserCode().equals(userCode)) throw new BusinessException(ErrorCode.CANNOT_SEARCH_OTHER_PRODUCT);

        List<ReqProduct> reqProductList = reqProductMapper.findByProductCode(productCode);

        reqReceived.setProduct(ReqProductReceivedReqListResponse.of(findProduct));

        reqProductList.stream()
//                .filter(reqProduct -> reqProduct.getChatStatus().equals(ChatStatus.REQ))
                .map(reqProduct -> {
                    Optional<User> userOptional = userMapper.findByUserCode(reqProduct.getRequesterCode());
                    return userOptional.map(user -> {
                        Location userLocation = locationMapper.findByLocationDongCd(user.getLocationDongCd());
                        String locationName = getLocationName(userLocation);
                        Optional<ChatRoom> chatRoomOptional = chatRoomMapper.findByProductReqCode(reqProduct.getProductReqCode());
                        String roomCode;
                        if (chatRoomOptional.isPresent()) {
                            roomCode = chatRoomOptional.get().getRoomCode();
                        } else {
                            roomCode = "";
                        }
                        return ReqUserProfile.of(user, reqProduct, roomCode, locationName);
                    });
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(reqReceived::addReqUserProfile);

        reqProductList.stream()
                .filter(reqProduct -> !reqProduct.getReqRead())
                .forEach(reqProduct -> reqProductMapper.saveReqRead(reqProduct.getProductReqCode()));
        return reqReceived;
    }

}
