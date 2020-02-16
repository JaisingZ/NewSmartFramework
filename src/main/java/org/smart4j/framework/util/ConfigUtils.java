package org.smart4j.framework.util;

import org.smart4j.framework.ConfigConstant;

import java.util.Properties;

/**
 * @Author: Jaising
 * @Description: 获取配置文件属性
 * @Date: Created in 2/15/2020 5:33 PM
 */
public final class ConfigUtils {

    // 加载配置文件
    private static final Properties CONFIG_PROPERTIES = PropertiesUtils.loadProperties(ConfigConstant.CONFIG_FILE);

    /**
     * 获取JDBC驱动
     */
    public static String getJDBCDriver() {
        return PropertiesUtils.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取JDBC URL
     */
    public static String getJDBCUrl() {
        return PropertiesUtils.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_URL);
    }

    /**
     * 获取JDBC用户名
     */
    public static String getJDBCUsername() {
        return PropertiesUtils.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_USERNAME);
    }

    /**
     * 获取JDBC密码
     */
    public static String getJDBCPassword() {
        return PropertiesUtils.getString(CONFIG_PROPERTIES, ConfigConstant.JDBC_PASSWORD);
    }

    /**
     * 获取基础包名
     */
    public static String getBasePackage() {
        return PropertiesUtils.getString(CONFIG_PROPERTIES, ConfigConstant.APP_BASE_PACKAGE);
    }
}
