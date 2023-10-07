package com.life.muna.productReq.dto.list;

import com.life.muna.productReq.dto.profile.ReqUserProfile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReqReceivedResponse {
    private ReqProductReceivedReqListResponse product;
    private final List<ReqUserProfile> reqUserProfile = new ArrayList<>();

    public void setProduct(ReqProductReceivedReqListResponse product) {
        this.product = product;
    }

    public void addReqUserProfile(ReqUserProfile reqUserProfile) {
        this.reqUserProfile.add(reqUserProfile);
    }

    public ReqReceivedResponse() {
    }
}
