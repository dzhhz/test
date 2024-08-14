package com.feilu.api.dao.system.entity;

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
 * @since 2024-08-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_order_aftersale_status_alias")
public class SystemOrderAftersaleStatusAlias implements Serializable {

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

    /**
     * 商家/用户处理提示
     */
    @Column(value = "handlerTitle")
    private String handlerTitle;

    /**
     * 提醒标题
     */
    @Column(value = "remindTitle")
    private String remindTitle;

    /**
     * 提醒描述 换行<br/>
     */
    @Column(value = "remindTitleDes")
    private String remindTitleDes;

}
