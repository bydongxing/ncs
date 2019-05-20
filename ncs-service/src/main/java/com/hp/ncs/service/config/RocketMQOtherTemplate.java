package com.hp.ncs.service.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 使用Rocketmq的原生api
 *
 * @author dongxing
 **/
@Component
@Slf4j
public class RocketMQOtherTemplate implements InitializingBean, DisposableBean {

    @Value("${spring.cloud.stream.rocketmq.binder.name-server}")
    private String nameServer;

    @Value("${spring.rocketmq.producer.group}")
    private String group;

    @Setter
    @Getter
    private DefaultMQProducer producer;


    @Override
    public void destroy() throws Exception {
        Optional.ofNullable(producer).ifPresent(DefaultMQProducer::shutdown);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Initializing  rocketmq: NameServer:" + nameServer);
        producer = new DefaultMQProducer();
        producer.setNamesrvAddr(nameServer);
        producer.setProducerGroup(group);
        producer.setInstanceName(Long.toString(System.currentTimeMillis()));
        producer.start();
    }
}