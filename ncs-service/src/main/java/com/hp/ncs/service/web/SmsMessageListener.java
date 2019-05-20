package com.hp.ncs.service.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息
 *
 * @author dongxing
 **/
@Slf4j
@Component
public class SmsMessageListener{

    @StreamListener("input1")
    public void receiveInput1(String receiveMsg,MessageExt messageExt) {
       System.out.println("-------------receive message："+receiveMsg);
    }
}
