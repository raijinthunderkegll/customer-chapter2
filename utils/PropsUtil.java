package com.chy.chapter2.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件工具类
 * Created by yang on 2017/7/16.
 */
public class PropsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName) {
        Properties properties = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + ">> file is not found");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            LOGGER.error("load properties failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                }catch (IOException e){
                    LOGGER.error("close input stream failure",e);
                }
            }
        }
        return properties;
    }
    /**
     * 获取字符型数据
     */
    public static String getStringVal(Properties properties, String key, String defaultValue){
        String value = defaultValue;
        if(properties.containsKey(key)){
            value = properties.getProperty(key);
        }
        return value;
    }
    /**
     * 获取数值型数据
     */

    /**
     * 获取布尔型数据
     */

    public static void main(String[] args) {
        Properties properties = PropsUtil.loadProps("jdbc.properties");
        String driver = PropsUtil.getStringVal(properties,"jdbc.driver","");
        System.out.println(driver);
    }
}
