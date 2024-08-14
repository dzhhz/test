package com.feilu.api.dao.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(value = "tb_order_tracking_status")
public class SystemOrderTrackingStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "parentId")
    private Integer parentId;

    @Column(value = "statusCode")
    private String statusCode;

    @Column(value = "statusNameCn")
    private String statusNameCn;

    @Column(value = "statusIcon")
    private String statusIcon;

    @Column(value = "orderIdx")
    private BigDecimal orderIdx;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

}
