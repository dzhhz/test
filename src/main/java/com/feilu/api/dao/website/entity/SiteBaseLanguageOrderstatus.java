package com.feilu.api.dao.website.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

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
@Table(value = "site_base_language_orderstatus")
public class SiteBaseLanguageOrderstatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "languageCode")
    private String languageCode;

    @Column(value = "statusCode")
    private String statusCode;

    @Column(value = "statusName")
    private String statusName;

}
