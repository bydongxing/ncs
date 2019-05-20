package com.hp.ncs.service.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 短信的消息体
 *
 * @author dongxing
 **/
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SMS消息实体")
public class SMSMessageVO implements Serializable {

    private static final long serialVersionUID = -114788599696220537L;

    /**
     * 收件人号码
     */
    @Valid
    private Phone phone;

    /**
     * 消息的code模板
     */
    @NotNull(message = "{templateCode.illegal}")
    @NotEmpty(message = "{templateCode.illegal}")
    @ApiModelProperty(value = "短信模版code值",required = true)
    private String templateCode;

    /**
     * 消息的级别
     */
    @ApiModelProperty("短信消息的发送级别")
    private transient Integer messageDelayLevel;

    /**
     * 消息的内容
     */
    @ApiModelProperty("短信模版里需要替换的代码和值队列")
    private Object templateValue;

    @Data
    @ApiModel(value = "手机的实体对象")
    public static class Phone{

        /**
         * 国家代码
         */
        @ApiModelProperty("国家代码")
        private String countryCode;

        /**
         * 收件号码
         */
        @Pattern(regexp="1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}",message = "{phone.illegal}")
        @ApiModelProperty(value = "手机号",required = true)
        private String phone;

    }
}
