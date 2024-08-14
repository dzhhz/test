package com.feilu.api.dao.website.entity;

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
 * @since 2024-08-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "site_base_site_payment")
public class SiteBaseSitePayment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "siteCode")
    private String siteCode;

    @Column(value = "paymentId")
    private Integer paymentId;

    @Column(value = "paymentNameAlias")
    private String paymentNameAlias;

    @Column(value = "orderIdx")
    private Integer orderIdx;

    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

}
