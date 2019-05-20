package com.hp.ncs.service.web;


import com.alibaba.fastjson.JSON;
import com.hp.ncs.common.Result;
import com.hp.ncs.common.common.Constant;
import com.hp.ncs.common.util.DateUtil;
import com.hp.ncs.common.util.StringUtil;
import com.hp.ncs.service.entity.po.Message;
import com.hp.ncs.service.entity.vo.SMSMessageVO;
import com.hp.ncs.service.enums.MessageEnum;
import com.hp.ncs.service.enums.StatusEnum;
import com.hp.ncs.service.enums.UserEnum;
import com.hp.ncs.service.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.antframework.common.util.id.Id;
import org.antframework.idcenter.client.Ider;
import org.antframework.idcenter.spring.IdersContexts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * <p>
 * 消息详情 前端控制器
 * </p>
 *
 * @author dongxing
 * @since 2019-05-15
 */
@RestController
@RequestMapping("/app/v1/messages")
@AllArgsConstructor(onConstructor_={@Autowired})
@Api(value = "消息的控制器")
public class MessageController{

    private final MessageService messageService;

    @PostMapping("/sms")
    @ApiOperation(value = "短信消息的接收入口")
    public Result receiveMessage(@Valid SMSMessageVO smsMessageVO, HttpServletRequest request){

        // 获取message-uid
        Ider ider = IdersContexts.getIder("message-uid");
        // 获取id
        Id id1 = ider.acquire();
        // 对id进行格式化，比如：2019022400001
        String formattedId1 = id1.getPeriod().toString() + String.format("%05d", id1.getId());

        Message message = Message.builder()
                .id(formattedId1)
                .appId("111111111111")
                .mqMsgId(StringUtil.getId())
                .receiveDate(DateUtil.getDate())
                .messageDelayLevel(Constant.DEFAULTLEVEL)
                .status(StatusEnum.RECEIVED)
                .messageClass(MessageEnum.SMS)
                .messageType(MessageEnum.SMS)
                .createdBy(UserEnum.APP)
                .build();

        if (Optional.ofNullable(smsMessageVO.getMessageDelayLevel()).isPresent()) {
            message.setMessageDelayLevel(smsMessageVO.getMessageDelayLevel());
        }
        if (StringUtils.isEmpty(smsMessageVO.getPhone().getCountryCode())) {
            smsMessageVO.getPhone().setCountryCode(Constant.CONTURY_CODE_DEFAULT);
        }
        message.setContent(JSON.toJSONString(smsMessageVO));
        messageService.sendWithKeys(message);
        return Result.createBySuccess(formattedId1);
    }
}
