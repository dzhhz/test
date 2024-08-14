package com.feilu.api.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class RangConditionsToTimeModelDTO implements Serializable {

    @Serial
	private static final long serialVersionUID = -1649669156877508723L;

    /**
     * 开始时间
     */
    private Timestamp beginTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;

}
