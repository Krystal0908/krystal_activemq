package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author krystal
 * @create 2023-08-23 0:43
 */
public class JmsProduce_Topic {
    public static final String ACTIVEMQ_URL = "tcp://192.168.28.150:61616";
    public static final String TOPIC_NAME = "topic-atguigu";

    public static void main(String[] args) throws JMSException {
        //1、创建连接工厂,按照给定的url地址，采用默认的用户名和密码（admin/admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接Connection,并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3、创建会话session
        //两个参数：第一个叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4、创建目的地，具体是队列queue还是主题topic
        //Destination类似：Collection collection = new ArrayList();
        //Destination destination = session.createQueue(QUEUE_NAME);

        Topic topic = session.createTopic(TOPIC_NAME);
        //5、创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);
        //6、通过messageProducer生产3条消息发送到消息队列中
        for (int i = 1; i <= 3; i++) {
            //7、创建消息
            TextMessage textMessage = session.createTextMessage("TOPIC_NAME---" + i);
            //8、通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }

        //9、关闭资源。顺着申请，倒着关闭
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("****TOPIC_NAME消息发布到MQ完成****");
    }
}
