package com.chas.telenortest.repositories;

import com.chas.telenortest.dataObjects.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
