package com.br.ambev.order.service;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.dto.OrderResponse;
import com.br.ambev.order.dto.ProductDto;
import com.br.ambev.order.repository.OrderRepository;
import com.br.ambev.order.repository.entity.Order;
import com.br.ambev.order.repository.entity.Product;
import com.br.ambev.order.service.impl.OrderServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    private OrderRequest createTestOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderNumber("order1");

        ProductDto product1 = new ProductDto();
        product1.setName("Product A");
        product1.setPrice(BigDecimal.valueOf(10.0));

        ProductDto product2 = new ProductDto();
        product2.setName("Product B");
        product2.setPrice(BigDecimal.valueOf(20.0));

        orderRequest.setProducts(Arrays.asList(product1, product2));
        return orderRequest;
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("order1");

        Product product1 = new Product();
        product1.setName("Product A");
        product1.setPrice(BigDecimal.valueOf(10.0));

        Product product2 = new Product();
        product2.setName("Product B");
        product2.setPrice(BigDecimal.valueOf(20.0));

        order.setProducts(Arrays.asList(product1, product2));
        return order;
    }


    @Test
    void testProcessOrderSuccess() {
        OrderRequest orderRequest = createTestOrderRequest();
        when(orderRepository.findByOrderNumber(anyString())).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(any());

        OrderResponse response = orderService.processOrder(orderRequest);

        assertEquals("order1", response.getOrderId());
        assertEquals(BigDecimal.valueOf(30.0), response.getTotalValue());
        assertEquals("Processed", response.getStatus());
        verify(orderRepository, times(1)).findByOrderNumber(any());
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void testProcessOrderDuplicate() {

        OrderRequest orderRequest = createTestOrderRequest();
        Order order = createTestOrder();

        when(orderRepository.findByOrderNumber(any())).thenReturn(Optional.of(order));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.processOrder(orderRequest);
        });
        assertEquals("Pedido duplicado: order1", exception.getMessage());
        verify(orderRepository, times(1)).findByOrderNumber(any());

    }


    @Test
    void testGetAllOrders() {
        Order orderEntity1 = createTestOrder();
        orderEntity1.setTotalValue(BigDecimal.valueOf(30.0));
        Order orderEntity2 = createTestOrder();
        orderEntity2.setTotalValue(BigDecimal.valueOf(50.0));
        when(orderRepository.findAll()).thenReturn(Arrays.asList(orderEntity1, orderEntity2));

        List<OrderResponse> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
        assertEquals("order1", orders.get(0).getOrderId());
        assertEquals(BigDecimal.valueOf(30.0), orders.get(0).getTotalValue());
        assertEquals("order1", orders.get(1).getOrderId());
        assertEquals(BigDecimal.valueOf(50.0), orders.get(1).getTotalValue());

    }
}
