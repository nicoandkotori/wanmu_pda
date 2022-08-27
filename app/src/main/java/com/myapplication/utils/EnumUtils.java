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
        //称重
        QUEUE_WEIGHT_ONE("QUEUE_Weight_One"),
        //起挂扫描芯片
        QUEUE_RFID_START_TO_HANG_OLD_ONE("QUEUE_START_TO_HANG_RFID_ONE"),
        //转挂扫描旧芯片
        QUEUE_RFID_MID_SECTION_OLD_ONE("QUEUE_MID_SECTION_RFID_OLD_ONE"),
        //转挂扫描新芯片
        QUEUE_RFID_MID_SECTION_NEW_ONE("QUEUE_MID_SECTION_RFID_NEW_ONE"),
        //二分体分切扫描旧芯片
        QUEUE_RFID_TETRAD_OLD_ONE("QUEUE_TETRAD_RFID_OLD_ONE"),
        //二分体分切扫描新芯片
        QUEUE_RFID_TETRAD_NEW_ONE("QUEUE_TETRAD_RFID_NEW_ONE");

        private final String value;

        RABBIT_MQ_QUEUE(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
