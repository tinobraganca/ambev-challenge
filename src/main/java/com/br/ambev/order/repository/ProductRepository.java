package com.br.ambev.order.repository;

import com.br.ambev.order.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
