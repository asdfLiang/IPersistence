package com.lagou.utils;

/**
 * @author liangzj
 * @date 2021/1/28 1:30
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
