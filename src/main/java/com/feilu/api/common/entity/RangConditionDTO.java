package com.feilu.api.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RangConditionDTO implements Serializable {

    private static final long serialVersionUID = 4872669865514539066L;
    /**
     * 开始区间
     */
    private String beginValue;

    /**
     * 结束区间
     */
    private String endValue;
}
