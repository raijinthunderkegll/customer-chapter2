package com.chy.chapter2.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * Created by yang on 2017/7/16.
 */
public class StringUtil {

    /**
     * 判断字符串是空
     */
    public static boolean isEmpty(String str){
        if(str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串非空
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * 首字母转大写
     */
    public static String captureString(String str){
/*        str = str.substring(0,1).toUpperCase() + str.substring(1);
        return str;*/
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    public static void main(String[] args) {
        String str = "    ";
        boolean res = StringUtil.isEmpty(str);
        System.out.println(res);
        System.out.println(StringUtil.isNotEmpty(str));


        long times = System.currentTimeMillis();
        String strr = "afdferersafdfadfa";
/*        for(int i=0;i<20;i++){
            strr += strr;
        }*/
        strr = captureString(strr);
        System.out.println(strr);
        System.out.println("execute captureString() use ;" + (System.currentTimeMillis()-times)+"ms*********");
    }

}
