package com.feilu.api.dao.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
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
@Table(value = "tb_order_ext")
public class SystemOrderExt implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id@Column(value = "orderId")
    private String orderId;

    /**
     * 订单状态
     */
    @Column(value = "sysOrderStatus")
    private String sysOrderStatus;

    /**
     * 订单子状态
     */
    @Column(value = "sysSubOrderStatus")
    private String sysSubOrderStatus;

    /**
     * 财务状态
     */
    @Column(value = "orderBillStatus")
    private String orderBillStatus;

    @Column(value = "expressNo")
    private String expressNo;

    /**
     * 货代公司退回单号
     */
    @Column(value = "returnedPno")
    private String returnedPno;

    /**
     * 货代公司
     */
    @Column(value = "shipperId")
    private Integer shipperId;

    /**
     * 海外快递ID,dict_express表
     */
    @Column(value = "expressId")
    private Integer expressId;

    /**
     * 快递状态
     */
    @Column(value = "expressStatus")
    private String expressStatus;

    /**
     * 快递同步状态 0未同步 1已同步完成 2全家超过28天 3承运商不相符 4单号创建错误
     */
    @Column(value = "expressSync")
    private Integer expressSync;

    /**
     * 快递同步时间
     */
    @Column(value = "expressSyncTime")
    private LocalDateTime expressSyncTime;

    /**
     * 快递同步时间,用于whatsapp的第一次提醒
     */
    @Column(value = "expressSyncWhatsAppTimeFirst")
    private LocalDateTime expressSyncWhatsAppTimeFirst;

    /**
     * 快递同步时间,用于whatsapp的第二次提醒
     */
    @Column(value = "expressSyncWhatsAppTime")
    private LocalDateTime expressSyncWhatsAppTime;

    /**
     * 快递同步结束时间
     */
    @Column(value = "expressSyncEndTime")
    private LocalDateTime expressSyncEndTime;

    /**
     * 第一次派送时间
     */
    @Column(value = "firstCourierSendTime")
    private LocalDateTime firstCourierSendTime;

    /**
     * 货代公司揽件时间
     */
    @Column(value = "shipperCollectingTime")
    private LocalDateTime shipperCollectingTime;

    /**
     * 51Tracking是否创建查询标记 0未创建 1已创建
     */
    @Column(value = "expressSyncTrackStatus")
    private Integer expressSyncTrackStatus;

    /**
     * 快递同步返回信息
     */
    @Column(value = "expressRemark")
    private String expressRemark;

    /**
     * 生成运单号的账号
     */
    @Column(value = "expressOwner")
    private String expressOwner;

    /**
     * 快递运费
     */
    @Column(value = "expressPrice")
    private BigDecimal expressPrice;

    /**
     * VT签收时间
     */
    @Column(value = "signTime")
    private LocalDateTime signTime;

    /**
     * VT发货时间
     */
    @Column(value = "sendTime")
    private LocalDateTime sendTime;

    /**
     * 面单PDF保存路径 或者保存优美用来打印面单的order_id
     */
    @Column(value = "expressPdfPath")
    private String expressPdfPath;

    /**
     * 是否有问题 0没有 1有
     */
    @Column(value = "isProblem")
    private Integer isProblem;

    /**
     * 若退货退款之前没有送达记录，则为1,(订单对账的标记)
     */
    @Column(value = "isUnusual")
    private Integer isUnusual;

    /**
     * whats app快递提醒状态：0未提醒 1提醒第一次 2提醒第二次 9已签收或拒签之类的情况结束快递提醒
     */
    @Column(value = "isExpressRemind")
    private Integer isExpressRemind;

    /**
     * 退货标记 0未退货 1全部退货 2部分退货
     */
    @Column(value = "returnFlag")
    private Integer returnFlag;

    /**
     * 问题类型
     */
    @Column(value = "problemType")
    private String problemType;

    /**
     * 待确认状态: 0未确认 -1地址待确认 1已确认
     */
    @Column(value = "confirmFlag")
    private Integer confirmFlag;

    /**
     * 2客户信息 3黑名单 4重复 5购买量大  6疫情 7订单因拒收率太高系统直接作废 8 待支付9其他
     */
    @Column(value = "confirmType")
    private String confirmType;

    /**
     * 备注信息
     */
    @Column(value = "customerRemark")
    private String customerRemark;

    /**
     * 系统备注
     */
    @Column(value = "systemRemark")
    private String systemRemark;

    /**
     * 0 未检查 1已检查
     */
    @Column(value = "systemCheckFlag")
    private Integer systemCheckFlag;

    /**
     * 客户确认状态 -3拒收消息-2无法送达 -1未送达 0未确认/未读 1已确认/发货 2已读 3修改地址 4不发货 10WA沟通中  11联系不上(第1次) 12电话号码错误 13客户取消 21联系不上(第2次) 22联系不上(第3次) 33修改地址WA沟通中 34修改地址电话号码错误 35修改地址联系不上(第1次) 36修改地址联系不上(第2次) 37修改地址联系不上(第3次) 38地址不详 97地址修改成功 98客户确认发货 100其他 101未发line 
     */
    @Column(value = "customerConfirmFlag")
    private Integer customerConfirmFlag;

    /**
     * 人工检查时间
     */
    @Column(value = "examineTime")
    private LocalDateTime examineTime;

    /**
     * 人工检查操作人ID 0未检查 其他ID
     */
    @Column(value = "examineUserId")
    private Integer examineUserId;

    /**
     * 请求接口下单结果 true false
     */
    @Column(value = "createPnoState")
    private String createPnoState;

    /**
     * 请求接口下单结果信息
     */
    @Column(value = "createPnoMsg")
    private String createPnoMsg;

    /**
     * 售后处理类型仅做标记 1:换货 2:仅退款 3:退货退款 4:申请作废 5.客诉 6.拒收 7.转寄单 10无需退回
     */
    @Column(value = "processType")
    private Integer processType;

    /**
     * 是否需要转寄 0否 1是
     */
    @Column(value = "isExchange")
    private Integer isExchange;

    /**
     * 转寄id
     */
    @Column(value = "exchangeId")
    private Integer exchangeId;

    /**
     * 定时任务自动拉黑是否执行过：0未执行 1已执行
     */
    @Column(value = "blacklistCheckFlag")
    private Integer blacklistCheckFlag;

    /**
     * 是否标记已完成(数据图表用)：0未标记 1已标记
     */
    @Column(value = "completeFlag")
    private Integer completeFlag;

    /**
     * 快递提醒状态  -3拒收消息-2无法送达 -1未送达 0未确认/未读 1已确认/发货 2已读 3要修改 4不发货   10沟通中  11联系不上(第1次) 12电话号码错误 13客户取消 14客户拒收 15地址错误 16快递未联系 21联系不上(第2次) 22联系不上(第3次) 23客户同意收,已通知快递派送 24客户同意收,快递未联系上 25客户要改约日期,已通知快递 26客户要改约日期日,未通知快递 27修改配送地址,已通知快递 28修改配送地址,未通知快递 29已通知客户 99客户已收货 100其他
     */
    @Column(value = "shipRemindFlag")
    private Integer shipRemindFlag;

    /**
     * 快递催收状态 -3拒收消息-2无法送达 -1未送达 0未催收 1已确认/发货 2已读 3要修改 4不发货   10沟通中  11联系不上(第1次) 12电话错误 13客户取消 14客户拒收 15地址错误 16快递未联系 17延迟收货 21联系不上(第2次) 22联系不上(第3次) 23客户同意收,已通知快递派送 24客户同意收,快递未联系上 25客户要改约日期,已通知快递 26客户要改约日期日,未通知快递 27修改配送地址,已通知快递 28修改配送地址,未通知快递 29已通知客户 30联系不上(第4次) 31联系不上(第5次) 32联系不上(第6次) 43Line信息1 44Line信息2(20H) 45Line消息已读,完成 46Line消息未读/Line无等等 95完成催收 96客户答应取货 99客户已收货 100其他
     */
    @Column(value = "shipUrgeFlag")
    private Integer shipUrgeFlag;

    @Column(value = "gmtCreated")
    private LocalDateTime gmtCreated;

    @Column(value = "gmtUpdate")
    private LocalDateTime gmtUpdate;

}
