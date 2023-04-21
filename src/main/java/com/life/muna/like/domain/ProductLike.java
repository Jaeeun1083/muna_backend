package com.life.muna.like.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class ProductLike {
    private Long productCode;
    private Long userCode;
    private Date insertDate;
    private Date updateDate;
}
