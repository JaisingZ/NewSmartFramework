package org.smart4j.framework.bean;

import java.util.Map;

/**
 * @Author: Jaising
 * @Description: 请求参数Bean
 * @Date: Created in 3/29/2020 10:15 AM
 */
public class Param {

    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 获取所有字段信息
     */
    public Map<String, Object> getMap() {
        return paramMap;
    }
}
