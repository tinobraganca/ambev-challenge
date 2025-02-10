package com.br.ambev.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Schema(description = "Resposta com informações do pedido salvo")
public class OrderResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 4380920604279802042L;

    @Schema(description = "Numero do pedido recebido", example = "12342DEPO")
    private String orderId;

    @Schema(description = "Valor total dos produtos somados", example = "33.2")
    private BigDecimal totalValue;

    @Schema(description = "Status do pedido", example = "Processed")
    private String status;

    public OrderResponse(String orderId, BigDecimal totalValue, String status) {
        this.orderId = orderId;
        this.totalValue = totalValue;
        this.status = status;
    }

}