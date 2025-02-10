package com.br.ambev.order.service;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse processOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();
}
