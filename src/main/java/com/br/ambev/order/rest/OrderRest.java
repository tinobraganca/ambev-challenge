package com.br.ambev.order.rest;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.dto.OrderResponse;
import com.br.ambev.order.queue.producer.OrderProducer;
import com.br.ambev.order.rest.swagger.IOrderRest;
import com.br.ambev.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "APIs de gerenciamento de pedidos")
@RestController
@RequestMapping("/orders")
public class OrderRest implements IOrderRest {

    private static final Logger logger = LoggerFactory.getLogger(OrderRest.class);
    private final OrderProducer orderProducer;
    private final OrderService orderService;

    public OrderRest(OrderProducer orderProducer, OrderService orderService) {
        this.orderProducer = orderProducer;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        logger.info("Received request to create order: {}", orderRequest.getOrderNumber());
        orderProducer.sendOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order request queued successfully.");
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        logger.info("Received request to get all orders");
        return orderService.getAllOrders();
    }
}
