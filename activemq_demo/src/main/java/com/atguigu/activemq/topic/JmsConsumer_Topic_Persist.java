package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author krystal
 * @create 2023-08-23 15:30
 */
public class JmsConsumer_Topic_Persist {

    public static final String ACTIVEMQ_URL = "tcp://192.168.28.150:61616";
//    public static final String ACTIVEMQ_URL = "tcp://10.10.10.160:61616";

    public static final String TOPIC_NAME = "Topic-jdbc-Persist";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*****z4");

        //1、创建连接工厂,按照给定的url地址，采用默认的用户名和密码（admin/admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接Connection,并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.setClientID("atguigu01");
        //3、创建会话session
        //两个参数：第一个叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地，具体是队列queue还是主题topic
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic,"mq-jdbc...");
        connection.start();

        Message message = topicSubscriber.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("*****收到的持久化topic：" + textMessage.getText());
             message = topicSubscriber.receive();
        }

        session.close();
        connection.close();

    }

}
