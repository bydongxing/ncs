package com.hp.ncs.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 *
 * @author dongxing
 **/
public enum  UserEnum {

    /**
     * app客户端
     */
    APP(0,"App"),
    /**
     * web界面
     */
    WEB(1,"Web");

    @EnumValue
    private Integer code;

    private String name;

    UserEnum(Integer code,String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
