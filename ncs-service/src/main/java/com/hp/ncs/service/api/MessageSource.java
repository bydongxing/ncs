package com.hp.ncs.service.api;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author dongxing
 **/
public interface MessageSource {

    @Input("input1")
    SubscribableChannel input1();
}
