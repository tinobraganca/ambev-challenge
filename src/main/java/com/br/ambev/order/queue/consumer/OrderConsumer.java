package com.br.ambev.order.queue.consumer;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private final OrderService orderService;

    public OrderConsumer( OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void receiveOrder(OrderRequest orderRequest) {
        logger.info("Received order request from queue: {}", orderRequest.getOrderNumber());
        try {
            orderService.processOrder(orderRequest);
            logger.info("Processed order: {}", orderRequest.getOrderNumber());
        } catch (Exception ex) {
            logger.error("Error processing order: {}", orderRequest.getOrderNumber(), ex);
        }
    }
}