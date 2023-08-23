package com.atguigu.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author krystal
 * @create 2023-08-22 21:34
 */
public class JmsConsumer {
    public static final String ACTIVEMQ_URL = "tcp://192.168.28.150:61616";

    public static final String QUEUE_NAME = "queue01";

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

        Queue queue = session.createQueue(QUEUE_NAME);

        //5、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /*
        同步阻塞方法receive()
        订阅者或接收者调用MessageConsumer的receive()方法来接收消息，receive()方法在能够接收到消息之前（或超时之前）将一直阻塞
        while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if (null != textMessage){
                System.out.println("******消费者接收到消息："+textMessage.getText());
            }else {
                break;
            }
        }
        //6、关闭资源。顺着申请，倒着关闭
        messageConsumer.close();
        session.close();
        connection.close();*/

        //通过监听的方式来消费消息      MessageConsumer messageConsumer = session.createConsumer(queue);
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("******消费者接收到消息：textMessage"+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        System.in.read();

        //关闭资源
        messageConsumer.close();
        session.close();
        connection.close();


        /**
         * 1 先生产，   启动1号消费者。问题：1号消费者还能消费消息吗？
         *  可以消费        Y
         *
         * 2 先生产，   启动1号消费者，再启动2号消费者，问题：2号消费者还能消费消息吗？
         *  2.1     1号可以消费      Y
         *  2.2     2号不可以消费     N
         *
         * 3 先启动2个消费者，再生产6条消息，请问，消费情况如何？
         *  3.1     2个消费者都有6条
         *  3.2     先到先得，6条全给一个
         *  3.3     一人一半        Y负载均衡，轮询
         *
         * 4 MQ宕机了，那么消息的持久化和丢失情况分别如何？
         *
         */

    }
}
