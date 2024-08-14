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
@Table(value = "tb_category_attribute")
public class SystemCategoryAttribute implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类目属性ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 类目ID
     */
    @Column(value = "categoryId")
    private Integer categoryId;

    /**
     * 属性名
     */
    @Column(value = "attrNameCn")
    private String attrNameCn;

    /**
     * 属性的输入类型.1:单选 2:多选 3:可输入
     */
    @Column(value = "inputType")
    private Integer inputType;

    /**
     * 是否关键属性
     */
    @Column(value = "isKeyAttr")
    private Integer isKeyAttr;

    /**
     * 是否销售属性
     */
    @Column(value = "isSaleAttr")
    private Integer isSaleAttr;

    /**
     * 是否颜色属性
     */
    @Column(value = "isColorAttr")
    private Integer isColorAttr;

    /**
     * 是否输入属性
     */
    @Column(value = "isInputAttr")
    private Integer isInputAttr;

    /**
     * 是否搜索属性
     */
    @Column(value = "isSearchAttr")
    private Integer isSearchAttr;

    /**
     * 是否带尺码表
     */
    @Column(value = "isSizeChartAttr")
    private Integer isSizeChartAttr;

    /**
     * 是否必须填选
     */
    @Column(value = "mustFlag")
    private Integer mustFlag;

    /**
     * 0关闭 1正常
     */
    private Integer status;

    /**
     * 排序
     */
    @Column(value = "orderIdx")
    private BigDecimal orderIdx;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
