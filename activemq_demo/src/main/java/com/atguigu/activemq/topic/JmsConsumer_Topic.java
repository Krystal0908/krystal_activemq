package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
@author krystal
@create 2023-08-23 0:46
*/
public class JmsConsumer_Topic {

    public static final String ACTIVEMQ_URL = "tcp://192.168.28.150:61616";

    public static final String TOPIC_NAME = "topic-atguigu";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*****我是1号消费者");

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

        //5、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        //通过监听的方式来消费消息      MessageConsumer messageConsumer = session.createConsumer(queue);
        //1、匿名内部类写法
        /*messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("******消费者接收到消息："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
        //2、lambda表达式写法
        messageConsumer.setMessageListener((Message message)->{
            if (null != message && message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("******消费者接收到Topic消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.in.read();

        //关闭资源
        messageConsumer.close();
        session.close();
        connection.close();

    }
}
