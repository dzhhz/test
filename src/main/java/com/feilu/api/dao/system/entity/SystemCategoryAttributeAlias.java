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
 * @since 2024-08-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_category_attribute_alias")
public class SystemCategoryAttributeAlias implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "languageCode")
    private String languageCode;

    @Column(value = "categoryId")
    private Integer categoryId;

    @Column(value = "attrId")
    private Integer attrId;

    @Column(value = "attrNameAlias")
    private String attrNameAlias;

    private Boolean status;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
