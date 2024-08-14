package com.feilu.api.dao.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;

/**
 *  实体类。
 *
 * @author dzh
 * @since 2024-08-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_order_blacklist")
public class SystemOrderBlacklist implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 站点
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 国家
     */
    @Column(value = "countryCode")
    private String countryCode;

    @Column(value = "blackName")
    private String blackName;

    @Column(value = "blackMobile")
    private String blackMobile;

    private String address1;

    private String address2;

    /**
     * 0失效 1有效
     */
    private Integer status;

    /**
     * 类型 0订单变为待确认 1订单因拒收率太高直接作废
     */
    private Integer type;

    /**
     * 操作人  0:系统2022年4月之前默认0
     */
    @Column(value = "optUserId")
    private Integer optUserId;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
