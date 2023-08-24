package com.at.boot.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

@Component
public class Topic_Consumer {

    @JmsListener(destination = "${mytopic}")
    public void receive(TextMessage textMessage) throws  Exception{

        try {
            System.out.println("消费者受到订阅的主题："+textMessage.getText());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
