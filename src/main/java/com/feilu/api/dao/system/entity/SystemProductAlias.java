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
@Table(value = "tb_product_alias")
public class SystemProductAlias implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "productId")
    private Integer productId;

    @Column(value = "languageCode")
    private String languageCode;

    private String title;

    private String summary;

    @Column(value = "keyWords")
    private String keyWords;

    private String tags;

    private String content;

    /**
     * 视频URL
     */
    @Column(value = "videoUrl")
    private String videoUrl;

    /**
     * 视频时长（秒）
     */
    @Column(value = "videoTimes")
    private Integer videoTimes;

    /**
     * 内容图片解析，计算宽高
     */
    @Column(value = "parseFlag")
    private Integer parseFlag;

    private Integer creator;

    private Integer editor;

    /**
     * 是否获取过评论 0:否 1:是
     */
    @Column(value = "aiReviewFlag")
    private Integer aiReviewFlag;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
