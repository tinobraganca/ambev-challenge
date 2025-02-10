package com.br.ambev.order.repository.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "product")
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 6097955954312780041L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Product(String name, BigDecimal price, Order order) {
        this.name = name;
        this.price = price;
        this.order = order;
    }

}