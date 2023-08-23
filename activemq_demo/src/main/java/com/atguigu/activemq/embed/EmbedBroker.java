package com.atguigu.activemq.embed;

import org.apache.activemq.broker.BrokerService;

/**
 * @author krystal
 * @create 2023-08-23 21:46
 */
public class EmbedBroker {
    public static void main(String[] args) throws Exception {
        //ActiveMQ也支持在vm中通信基于嵌入式的broker
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();


    }
}
