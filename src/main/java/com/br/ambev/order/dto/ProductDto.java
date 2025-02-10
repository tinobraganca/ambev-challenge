package com.br.ambev.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Produto do pedido")
public class ProductDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 9092712856644758965L;

    @Schema(description = "Nome do produto", example = "PRODUTONOVO")
    private String name;

    @Schema(description = "Preco do produto", example = "11.2")
    private BigDecimal price;
}
