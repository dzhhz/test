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
 * @since 2024-08-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_log_order")
public class SystemLogOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单操作日志表
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 订单ID
     */
    @Column(value = "orderId")
    private String orderId;

    /**
     * 状态
     */
    @Column(value = "statusFlag")
    private String statusFlag;

    /**
     * 扫码配货时用的配货架ID
     */
    @Column(value = "shelfId")
    private Integer shelfId;

    /**
     * 操作人ID 0:客户  -1:系统
     */
    @Column(value = "optUserId")
    private Integer optUserId;

    /**
     * 理由/原因
     */
    private String reason;

    /**
     * 备注/描述
     */
    private String remark;

    /**
     * ext中的原状态
     */
    @Column(value = "sysOrderStatus")
    private String sysOrderStatus;

    /**
     * order中的原状态
     */
    @Column(value = "orderStatus")
    private String orderStatus;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

}
