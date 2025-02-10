package com.br.ambev.order.service.impl;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.dto.OrderResponse;
import com.br.ambev.order.dto.ProductDto;
import com.br.ambev.order.repository.OrderRepository;
import com.br.ambev.order.repository.entity.Order;
import com.br.ambev.order.repository.entity.Product;
import com.br.ambev.order.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse processOrder(OrderRequest orderRequest) {

        try {
        logger.info("Processing order: {}", orderRequest.getOrderNumber());
        Optional<Order> order = orderRepository.findByOrderNumber(orderRequest.getOrderNumber());
        if (order.isPresent()) {
            logger.error("Duplicate order: {}", orderRequest.getOrderNumber());
            throw new IllegalArgumentException("Pedido duplicado: " + orderRequest.getOrderNumber());
        }

        BigDecimal orderValue = this.sumOrderValues(orderRequest);

        Order newOrder = Order.builder()
                .orderNumber(orderRequest.getOrderNumber())
                .totalValue(orderValue)
                .status("Processed")
                .build();


        List<Product> products = orderRequest.getProducts().stream()
                .map(product -> new Product(product.getName(), product.getPrice(), newOrder))
                .collect(Collectors.toList());

        newOrder.setProducts(products);
        Order orderSaved = orderRepository.save(newOrder);

            logger.info("Order {} processed with total value: {}", newOrder, newOrder.getTotalValue());
            return new OrderResponse(newOrder.getOrderNumber(), newOrder.getTotalValue(), newOrder.getStatus());
        } catch (Exception ex) {
            logger.error("Error processing order: {}", orderRequest.getOrderNumber(), ex);
            throw ex;
        }
    }

    private BigDecimal sumOrderValues(OrderRequest orderRequest) {
        return orderRequest.getProducts().stream()
                .map(ProductDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<OrderResponse> getAllOrders() {
        try {
            logger.info("Fetching all orders");
            return orderRepository.findAll().stream()
                    .map(order -> new OrderResponse(order.getOrderNumber(), order.getTotalValue(), order.getStatus()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            logger.error("Error fetching orders", ex);
            throw ex;
        }
    }
}
