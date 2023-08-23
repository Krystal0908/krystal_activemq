package com.atguigu.activemq.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.TextMessage;

/**
 * @author krystal
 * @create 2023-08-23 22:07
 */
@Service
public class SpringMQ_Produce {

    @Resource
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-activemq.xml");
        SpringMQ_Produce produce = (SpringMQ_Produce) ctx.getBean("springMQ_Produce");

        /*
        匿名内部类
        produce.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("****spring和MQ的整合case...........");
                return textMessage;
            }
        });*/

        produce.jmsTemplate.send((session) -> {
            TextMessage textMessage = session.createTextMessage("****spring和MQ的整合case111...........");
            return textMessage;
        });

        System.out.println("*********send task over");



    }
}
