package org.smart4j.framework.bean;

/**
 * @Author: Jaising
 * @Description: 返回JSON数据Bean
 * @Date: Created in 3/29/2020 10:17 AM
 */
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
