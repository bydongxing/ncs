package com.hp.ncs.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hp.ncs.service.config.RocketMQOtherTemplate;
import com.hp.ncs.service.dao.MessageMapper;
import com.hp.ncs.service.entity.po.Message;
import com.hp.ncs.service.enums.StatusEnum;
import com.hp.ncs.service.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 消息详情 服务实现类
 * </p>
 *
 * @author dongxing
 * @since 2019-05-15
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private RocketMQOtherTemplate rocketMQOtherTemplate;

    @Resource
    private  MessageMapper messageMapper;

    @Value("${spring.cloud.stream.bindings.input1.destination}")
    private String smsMessage;

    @Override
    public void sendWithKeys(Message msg) {

        //保存DB
         messageMapper.insert(msg);

        //发送消息去MQ
        org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message(smsMessage, null,msg.getMqMsgId(), msg.getContent().getBytes());
        message.setDelayTimeLevel(msg.getMessageDelayLevel());

        try {
            rocketMQOtherTemplate.getProducer().send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("send message to mq success! message: "+ message);
                    msg.setStatus(StatusEnum.QUEUING);
                    //更新db的状态
                    messageMapper.updateById(msg);
                }
                @Override
                public void onException(Throwable throwable) {
                    log.error("send message to mq exception: "+throwable.getMessage()+", message: "+message);
                    msg.setStatus(StatusEnum.FAILED);
                    msg.setReason(throwable.getMessage());
                    //更新db的状态
                    messageMapper.updateById(msg);
                }
            });
        } catch (MQClientException e) {
            log.error("MQ Client Exception: "+e.getMessage()+", message: "+message);
            msg.setStatus(StatusEnum.FAILED);
            msg.setReason(e.getMessage());
            //更新db的状态
            messageMapper.updateById(msg);
        } catch (RemotingException e) {
            log.error("MQ Connection Exception: "+e.getMessage()+", message: "+message);
            msg.setStatus(StatusEnum.FAILED);
            msg.setReason(e.getMessage());
            //更新db的状态
            messageMapper.updateById(msg);
        } catch (InterruptedException e) {
            log.error("MQ Interrupted Exception: "+e.getMessage()+", message: "+message);
            msg.setStatus(StatusEnum.FAILED);
            msg.setReason(e.getMessage());
            //更新db的状态
            messageMapper.updateById(msg);
        }
    }
}
