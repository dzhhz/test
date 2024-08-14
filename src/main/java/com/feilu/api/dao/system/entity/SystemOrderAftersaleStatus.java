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
@Table(value = "tb_order_aftersale_status")
public class SystemOrderAftersaleStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "statusCode")
    private String statusCode;

    @Column(value = "statusNameCn")
    private String statusNameCn;

    /**
     * 1cancel 2refund 3change
     */
    @Column(value = "statusType")
    private Integer statusType;

    @Column(value = "orderIdx")
    private Integer orderIdx;

    /**
     * 对应售后状态 0:待审核  1:已处理  -2已取消 -1已拒绝 2审核通过 3处理中 4返货中 5退货完成 6退款完成 7换货完成
     */
    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

}
