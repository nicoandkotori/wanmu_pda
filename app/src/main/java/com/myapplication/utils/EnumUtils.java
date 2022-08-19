package com.myapplication.utils;

/**
 * 枚举工具类
 *
 * @author mijiahao
 * @date 2022/08/10
 */
public interface EnumUtils {


    /**
     * RabbitMq队列
     *
     * @author mijiahao
     * @date 2022/08/10
     */
    enum RABBIT_MQ_QUEUE {
        QUEUE_WEIGHT_ONE("QUEUE_Weight_One");

        private final String value;

        RABBIT_MQ_QUEUE(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
