package com.atguigu.activemq.queue.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author krystal
 * @create 2023-08-23 16:41
 */
public class JmsConsumer_TX {

    public static final String ACTIVEMQ_URL = "tcp://10.10.10.160:61616";

    public static final String QUEUE_NAME = "tx01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*****我是1号消费者");

        //1、创建连接工厂,按照给定的url地址，采用默认的用户名和密码（admin/admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接Connection,并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3、创建会话session
        //两个参数：第一个叫事务，第二个叫签收
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        //4、创建目的地，具体是队列queue还是主题topic
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(2000L);
            if (null != textMessage){
                System.out.println("******消费者接收到消息："+textMessage.getText());
                textMessage.acknowledge();
            }else {
                break;
            }
        }
        //6、关闭资源。顺着申请，倒着关闭
        messageConsumer.close();
        //事务提交
        session.commit();
        session.close();
        connection.close();
    }

}
