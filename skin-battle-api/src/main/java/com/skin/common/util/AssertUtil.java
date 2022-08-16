package com.skin.common.util;

import com.tzj.module.easyopen.exception.ApiException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class AssertUtil {

    //如果不是true，则抛异常
    public static void isFalse(boolean expression, String msg) {
        if (!expression) {
            throw new ApiException(msg);
        }
    }

    //如果是true，则抛异常
    public static void isTrue(boolean expression, String msg) {
        if (expression) {
            throw new ApiException(msg);
        }
    }

    //如果是空或者空字符串，则抛异常
    public static void isBlank(String str, String msg) {
        if (StringUtils.isBlank(str)) {
            throw new ApiException(msg);
        }
    }







    //如果集合为空或者长度小于1，则抛异常
    public static void isEmpty(Map map, String msg) {
        if (MapUtils.isEmpty(map)) {
            throw new ApiException(msg);
        }
    }

    //如果对象时空，则抛异常
    public static void isNull(Object object, String msg) {
        if (object == null) {
            throw new ApiException(msg);
        }
    }
    public static void isNotNull(Object object, String msg) {
        if (object != null) {
            throw new ApiException(msg);
        }
    }


}