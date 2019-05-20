package com.hp.ncs.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hp.ncs.service.entity.po.Message;

/**
 * <p>
 * 消息详情 服务类
 * </p>
 *
 * @author dongxing
 * @since 2019-05-15
 */
public interface MessageService extends IService<Message> {

    public void sendWithKeys(Message msg);
}
