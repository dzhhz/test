package com.feilu.api.dao.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
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
 * @since 2024-08-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_order_consignee")
public class SystemOrderConsignee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @Id@Column(value = "orderId")
    private String orderId;

    @Column(value = "siteCode")
    private String siteCode;

    @Column(value = "countryCode")
    private String countryCode;

    @Column(value = "addressId")
    private String addressId;

    @Column(value = "fullName")
    private String fullName;

    @Column(value = "firstName")
    private String firstName;

    @Column(value = "lastName")
    private String lastName;

    private String mobile;

    private String country;

    private String province;

    private String city;

    private String district;

    private String address1;

    private String address2;

    private String zipcode;

    private String email;

    /**
     * 配送方式  1:宅配到家  2:超市取货
     */
    @Column(value = "deliveryType")
    private Integer deliveryType;

    /**
     *  family:全家  711:七天
     */
    @Column(value = "marketType")
    private String marketType;

    /**
     * 商超名
     */
    @Column(value = "marketName")
    private String marketName;

    /**
     * 商超地址
     */
    @Column(value = "marketAddress")
    private String marketAddress;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
