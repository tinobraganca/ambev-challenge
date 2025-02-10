package com.br.ambev.order.queue.producer;

import com.br.ambev.order.dto.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrderProducer.class);
    private final String queueName;
    public OrderProducer(@Value("${spring.rabbitmq.template.default-receive-queue}") String queueName,  RabbitTemplate rabbitTemplate) {
        this.queueName = queueName;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(OrderRequest orderRequest) {
        try {
            logger.info("Sending order {} to queue", orderRequest.getOrderNumber());
            rabbitTemplate.convertAndSend(queueName, orderRequest);
            logger.info("Order {} sent to queue", orderRequest.getOrderNumber());
        } catch (Exception e){
            logger.error("ERRO:::: Order {} to queue", orderRequest.getOrderNumber());
            logger.error("Error sending order {} to queue", orderRequest.getOrderNumber(), e);
            throw e;
        }
    }
}