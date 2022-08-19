package com.myapplication.utils;

import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMq工厂类,，用于获取ConnectionFactory
 *
 * @author mijiahao
 * @date 2022/08/10
 */
public class RabbitMqFactory {

    private final static String USER_NAME = "guest";
    private final static String PASS_WORD = "guest";
    private final static String HOST_NAME = "116.62.236.254";
    private final static Integer PORT = 9002;

    private  static volatile ConnectionFactory connectionFactory;

    private RabbitMqFactory(){};

    /**
     * 单例模式，双重检查锁
     *
     * @return {@link ConnectionFactory}
     */
    public static ConnectionFactory getRabbitMqFactory(){
        if (connectionFactory ==  null){
            synchronized (RabbitMqFactory.class){
                if (connectionFactory == null){
                    ConnectionFactory connectionFactory = new ConnectionFactory();
                    connectionFactory.setUsername(USER_NAME);
                    connectionFactory.setPassword(PASS_WORD);
                    connectionFactory.setHost(HOST_NAME);
                    connectionFactory.setPort(PORT);
                    RabbitMqFactory.connectionFactory = connectionFactory;
                    return connectionFactory;
                }
            }
        }
        return connectionFactory;
    }
}
