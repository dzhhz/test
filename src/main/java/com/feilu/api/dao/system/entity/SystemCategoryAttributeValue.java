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
 * @since 2024-08-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_category_attribute_value")
public class SystemCategoryAttributeValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 类目ID
     */
    @Column(value = "categoryId")
    private Integer categoryId;

    /**
     * 类目属性ID
     */
    @Column(value = "attrId")
    private Integer attrId;

    /**
     * 类目属性值
     */
    @Column(value = "attrValueCn")
    private String attrValueCn;

    @Column(value = "colorValue")
    private String colorValue;

    /**
     * 产品码标签
     */
    @Column(value = "labelCode")
    private String labelCode;

    /**
     * 排序
     */
    @Column(value = "orderIdx")
    private BigDecimal orderIdx;

    /**
     * 是否在搜索中显示
     */
    @Column(value = "isSearch")
    private Integer isSearch;

    private Integer status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
