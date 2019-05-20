package com.hp.ncs.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 消息状态的枚举类
 *
 * @author dongxing
 */
public enum StatusEnum{

    /*
    启用
     */
    ENABLE(0,"启用"),
    /*
    禁用
     */
    DISABLE(1,"启用"),
    /*
    消息收到入库
     */
    RECEIVED(102001,"消息收到入库"),
    /*
    消息在MQ队列排队中
     */
    QUEUING(102002,"消息在MQ队列排队中"),
    /*
    消息已经调用第三方服务发送中
     */
    SENDING(102003,"消息已经调用第三方服务发送中"),
    /*
    消息已经成功发送
     */
    SUCCESSED(102004,"消息已经成功发送"),
    /*
    消息已经取消发送
     */
    CANCELED(102005,"消息已经取消发送"),
    /*
    消息发送失败
     */
    FAILED(102006,"消息发送失败");


    @EnumValue
    private int code;

    private String desc;

    StatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
