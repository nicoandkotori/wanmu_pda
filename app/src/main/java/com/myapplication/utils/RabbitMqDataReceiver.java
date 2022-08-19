package com.myapplication.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.myapplication.WeighQueueActivity;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMq工具类
 *
 * @author mijiahao
 * @date 2022/08/10
 */
public class RabbitMqDataReceiver {

    private Connection connection;



    /**
     * 收消息
     */
    public void beginConsume(EnumUtils.RABBIT_MQ_QUEUE queue,Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //建立连接
                    if (connection == null){
                        connection  = RabbitMqFactory.getRabbitMqFactory().newConnection();
                    }
                    //通道
                    final Channel channel = connection.createChannel();
                    channel.basicConsume(queue.getValue(), false, new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            super.handleDelivery(consumerTag, envelope, properties, body);
                            //接收到的消息
                            String msg = new String(body, "UTF-8");
                            //交付标记
                            long deliveryTag = envelope.getDeliveryTag();
                            System.out.println("receive msg:" + msg);
                            Message message = Message.obtain();
                            message.obj = msg;
                            handler.sendMessage(message);
                            channel.basicAck(deliveryTag, false);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * 请在activity销毁时关闭连接
     */
    public void closeConnection(){
        if (connection != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(this.getClass().getName(),"RabbitMq连接关闭失败");
                    }
                }
            }).start();

        }
    }

}
