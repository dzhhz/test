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
 * @since 2024-08-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "site_area_gr")
public class SiteAreaGr implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String province;

    @Column(value = "provinceEn")
    private String provinceEn;

    @Column(value = "firstLetterP")
    private String firstLetterP;

    private String city;

    @Column(value = "cityEn")
    private String cityEn;

    @Column(value = "firstLetterC")
    private String firstLetterC;

    private String district;

    @Column(value = "districtEn")
    private String districtEn;

    @Column(value = "firstLetterD")
    private String firstLetterD;

    private String zipcode;

    private Integer status;

}
