package com.br.ambev.order.rest;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.dto.OrderResponse;
import com.br.ambev.order.queue.producer.OrderProducer;
import com.br.ambev.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRest.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderProducer orderProducer;

    @MockBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateOrderSuccess() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderNumber("order1");
        orderRequest.setProducts(Collections.emptyList());

        // Simula o envio sem erro
        doNothing().when(orderProducer).sendOrder(any(OrderRequest.class));

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order request queued successfully."));
    }

    @Test
    void testGetAllOrdersSuccess() throws Exception {
        OrderResponse orderResponse = new OrderResponse("order1", BigDecimal.valueOf(30.0), "Processed");
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(orderResponse));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value("order1"))
                .andExpect(jsonPath("$[0].totalValue").value(30.0))
                .andExpect(jsonPath("$[0].status").value("Processed"));
    }
}