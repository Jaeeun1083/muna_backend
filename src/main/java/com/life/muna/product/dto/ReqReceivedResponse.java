package com.life.muna.product.dto;

import com.life.muna.user.dto.OtherUserProfile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReqReceivedResponse {
    private ProductRegiListResponse product;
    private final List<OtherUserProfile> otherUserProfiles = new ArrayList<>();

    public void setProduct(ProductRegiListResponse product) {
        this.product = product;
    }

    public void addReqUserProfile(OtherUserProfile otherUserProfile) {
        this.otherUserProfiles.add(otherUserProfile);
    }

    public ReqReceivedResponse() {
    }
}
