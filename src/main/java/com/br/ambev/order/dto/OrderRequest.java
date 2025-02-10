package com.br.ambev.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.*;


@Data
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Request com informações do pedido")
public class OrderRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7582578839794511813L;

    @Schema(description = "Numero do pedido recebido", example = "12342DEPO")
    private String orderNumber;

    @Schema(description = "Numero do pedido recebido")
    private List<ProductDto> products;
}