package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: Jaising
 * @Description: 配置文件工具类
 * @Date: Created in 2/15/2020 5:37 PM
 */
public class PropertiesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Properties.class);

    /**
     * 加载配置文件
     *
     * @param fileName 文件名称
     * @return {@link Properties}
     */
    public static Properties loadProperties(String fileName) {
        Properties properties;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (null == is) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            LOGGER.error("加载属性文件出错", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("释放资源出错", e);
                }
            }
        }
        return null;
    }

    /**
     * 获取字符型属性，默认是为空字符串
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    /**
     * 获取字符型属性
     * @param defaultValue  默认值
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.contains(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性，默认值为0
     */
    public static int getInt(Properties properties, String key) {
        return getInt(properties, key, 0);
    }

    /**
     * 获取数值型属性
     * @param defaultValue 默认值
     */
    public static int getInt(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if (properties.contains(key)) {
            try {
                value = Integer.parseInt(properties.getProperty(key));
            } catch (NumberFormatException ignored) {
            }
        }
        return value;
    }

    /**
     * 获取布尔型属性，默认false
     */
    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, false);
    }

    /**
     * 获取布尔型属性
     * @param defaultValue  默认值
     */
    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (properties.contains(key)) {
            try {
                value = Boolean.parseBoolean(properties.getProperty(key));
            } catch (Exception ignored) {
            }
        }
        return value;
    }
}
