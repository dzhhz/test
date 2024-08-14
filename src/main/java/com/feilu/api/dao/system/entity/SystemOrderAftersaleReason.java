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
@Table(value = "tb_order_aftersale_reason")
public class SystemOrderAftersaleReason implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "reasonCode")
    private String reasonCode;

    @Column(value = "reasonNameCn")
    private String reasonNameCn;

    /**
     * 1cancel 2refund 3change
     */
    @Column(value = "reasonType")
    private String reasonType;

    @Column(value = "orderIdx")
    private Integer orderIdx;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

}
