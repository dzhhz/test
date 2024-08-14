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
@Table(value = "tb_member_whatsapp_business")
public class SystemMemberWhatsappBusiness implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 应用id
     */
    @Column(value = "appId")
    private String appId;

    /**
     * 应用秘钥
     */
    @Column(value = "appSecret")
    private String appSecret;

    /**
     * 应用名称
     */
    @Column(value = "appName")
    private String appName;

    /**
     * 应用编号
     */
    @Column(value = "appNumber")
    private String appNumber;

    /**
     * 手机编号
     */
    @Column(value = "phoneNumber")
    private String phoneNumber;

    /**
     * 站点
     */
    @Column(value = "siteCode")
    private String siteCode;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 版本
     */
    private String version;

    /**
     * 应用token
     */
    @Column(value = "accessTokenApp")
    private String accessTokenApp;

    /**
     * token
     */
    @Column(value = "accessToken")
    private String accessToken;

    /**
     * token长期
     */
    @Column(value = "accessTokenLong")
    private String accessTokenLong;

    /**
     * webhooks验证口令
     */
    @Column(value = "verifyToken")
    private String verifyToken;

    /**
     * 质量 HIGH LOW MEDIUM UNKNOWN
     */
    @Column(value = "qualityRating")
    private String qualityRating;

    /**
     * 发送量级 "LIMIT_NA" "LIMIT_50" "LIMIT_250" "LIMIT_1K" "LIMIT_10K" "LIMIT_100K" "UNLIMITED
     */
    @Column(value = "currentLimit")
    private String currentLimit;

    /**
     * 账号状态 "BANNED" "CONNECTED" "DELETED" "DISCONNECTED" "FLAGGED" "MIGRATED" "PENDING" "RATE_LIMITED" "RESTRICTED" "UNKNOWN" "UNVERIFIED"
     */
    @Column(value = "accountStatus")
    private String accountStatus;

    /**
     * 0已停用  1已启用
     */
    private Integer status;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 回调地址
     */
    @Column(value = "notifyUrl")
    private String notifyUrl;

    @Column(value = "gmtCreate")
    private LocalDateTime gmtCreate;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
