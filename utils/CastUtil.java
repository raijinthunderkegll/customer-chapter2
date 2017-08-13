package com.chy.chapter2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类型转换工具类
 * Created by yang on 2017/7/16.
 */
public class CastUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastUtil.class);

    /**
     * 转为String
     */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    public static String castString(Object obj) {
        return castString(obj, "");
    }

    /**
     * 转为double
     */
    public static double castDouble(Object obj, double defaultValue) {
        double value = defaultValue;
        if (obj != null) {
            String str = CastUtil.castString(obj);
            if (StringUtil.isNotEmpty(str)) {
                try {
                    Double.parseDouble(str);
                } catch (NumberFormatException e) {
                    LOGGER.error(str + " can't cast to double, choose default value " + defaultValue);
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }

    /**
     * 转为long
     */
    public static long castLong(Object obj, long defaultValue) {
        long value = defaultValue;
        String str = castString(obj);
        if (StringUtil.isNotEmpty(str)) {
            try {
                Long.parseLong(str);
            } catch (NumberFormatException e) {
                LOGGER.error(str + " can't cast to long,choose default value " + defaultValue);
                value = defaultValue;
            }
        }
        return value;
    }

    public static long castLong(Object obj) {
        return castLong(obj, 0);
    }

    /**
     * 转为int
     */
    public static int castInt(Object obj, int defaultValue) {
        int value = defaultValue;
        String str = castString(obj);
        if (StringUtil.isNotEmpty(str)) {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                LOGGER.error(str + " can't cast to int,choose default value " + defaultValue);
                value = defaultValue;
            }
        }
        return value;
    }

    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }

    /**
     * 转为boolean
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean value = defaultValue;
        String str = castString(obj);
        value = Boolean.parseBoolean(str);
        return value;
    }

    public static boolean castBoolean(Object obj){
        return castBoolean(obj,false);
    }
}
