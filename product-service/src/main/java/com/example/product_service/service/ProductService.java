package com.example.product_service.service;

import com.example.product_service.entity.Product;
import com.example.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

  private final ProductRepository repository;

  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  public List<Product> findAll() {
    return repository.findAll();
  }

  public Product findById(Long id) {
    return repository.findById(id)
        .orElseThrow();
  }

  public Product save(Product product) {
    return repository.save(product);
  }
}