package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author krystal
 * @create 2023-08-23 15:30
 */
public class JmsProduce_Topic_Persist {

    public static final String ACTIVEMQ_URL = "tcp://192.168.28.150:61616";
//    public static final String ACTIVEMQ_URL = "tcp://10.10.10.160:61616";
    public static final String TOPIC_NAME = "Topic-jdbc-Persist";

    public static void main(String[] args) throws JMSException {
        //1、创建连接工厂,按照给定的url地址，采用默认的用户名和密码（admin/admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接Connection,并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        //3、创建会话session
        //两个参数：第一个叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地，具体是队列queue还是主题topic
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageProducer messageProducer = session.createProducer(topic);
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        connection.start();
        for (int i = 1; i <= 3; i++) {
            //7、创建消息
            TextMessage textMessage = (TextMessage) session.createTextMessage("msg-persist-" + i);
            //8、通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }

        //9、关闭资源。顺着申请，倒着关闭
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("****带持久化的topic消息发布到MQ完成****");
    }

    /**
     * 注意：
     * 1.	一定要先运行一次消费者，等于向MQ注册，类似我订阅了这个主题。
     * 2.	然后再运行生产者发送消息。
     * 3.	之后无论消费者是否在线，都会收到消息。如果不在线的话，下次连接的时候，会把没有收过的消息都接收过来。
     */

}
