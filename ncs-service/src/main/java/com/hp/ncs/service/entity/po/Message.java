package com.hp.ncs.service.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hp.ncs.service.enums.MessageEnum;
import com.hp.ncs.service.enums.StatusEnum;
import com.hp.ncs.service.enums.UserEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息详情
 * </p>
 *
 * @author dongxing
 * @since 2019-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 设备信息的id
     */
    private String appId;

    /**
     * MQ的消息id
     */
    private String mqMsgId;

    /**
     * 接收消息的时间
     */
    private Date receiveDate;

    /**
     * 推送消息给MQ的时间

     */
    private Date queuingDate;

    /**
     * 发送的消息的时间
     */
    private Date sendDate;

    /**
     * 取消的时间
     */
    private Date cancelDate;

    /**
     * 消息的级别
     */
    private Integer messageDelayLevel;

    /**
     * 消息的状态
     */
    private StatusEnum status;

    /**
     * 取消发送消息的原因

     */
    private String reason;

    /**
     * 消息类型
SMS和Email
     */
    private MessageEnum messageClass;

    /**
     * 消息类别
SMS和Email
     */
    private MessageEnum messageType;

    /**
     * 消息的收件人等相关的信息
     */
    private String content;

    /**
     * 消息的创建时间
     */
    private Date createTime;

    /**
     * 消息的修改时间
     */
    private Date updateTime;

    /**
     * 消息的创建者
     */
    private UserEnum createdBy;

    /**
     * 消息的修改者
     */
    private UserEnum updateBy;


}
