package com.atguigu.activemq.queue.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author krystal
 * @create 2023-08-23 16:41
 */
public class JmsProduce_TX {

    public static final String ACTIVEMQ_URL = "tcp://10.10.10.160:61616";
    public static final String QUEUE_NAME = "tx01";

    public static void main(String[] args) throws JMSException {
        //1、创建连接工厂,按照给定的url地址，采用默认的用户名和密码（admin/admin）
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2、通过连接工厂，获得连接Connection,并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3、创建会话session
        //两个参数：第一个叫事务，第二个叫签收
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //4、创建目的地，具体是队列queue还是主题topic
        Queue queue = session.createQueue(QUEUE_NAME);
        //5、创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        //6、通过messageProducer生产3条消息发送到消息队列中
        for (int i = 1; i <= 3; i++) {
            //7、创建消息
            TextMessage textMessage = session.createTextMessage("tx msg---" + i);//理解为一个字符串

            //8、通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }

        //9、关闭资源。顺着申请，倒着关闭
        messageProducer.close();
        //事务的提交
        session.commit();
        session.close();
        connection.close();
        System.out.println("****tx消息发送到MQ完成****");


        /*try {
            //ok session.commit;
        } catch (Exception e) {
            e.printStackTrace();
            //error
            session.rollback();
        } finally {
            if (null != session) {
                session.close();
            }
        }*/

    }

}
