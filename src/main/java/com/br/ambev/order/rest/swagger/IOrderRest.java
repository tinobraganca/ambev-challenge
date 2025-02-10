package com.br.ambev.order.rest.swagger;

import com.br.ambev.order.dto.OrderRequest;
import com.br.ambev.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IOrderRest {
    @Operation(description = "Cria pedido na fila")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido criado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderRequest.class))),

    })
    ResponseEntity<String> createOrder(OrderRequest orderRequest);


    @Operation(description = "Busca todos os pedido já processados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca pedidos processados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))),

    })
    List<OrderResponse> getAllOrders();
}
