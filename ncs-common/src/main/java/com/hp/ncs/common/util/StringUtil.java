package com.hp.ncs.common.util;

import java.util.UUID;

/**
 * @author dongxing
 **/
public class StringUtil {

    public static String getId() {
        int i = (int)(Math.random()*900 + 100);
        return UUID.randomUUID().toString()+"-"+ i;
    }
}
