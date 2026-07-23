package com.example.product_service.controller;

import com.example.product_service.entity.Product;
import com.example.product_service.service.ProductService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public List<Product> getProducts() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Product getProduct(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  public Product create(@RequestBody Product product) {
    return service.save(product);
  }
}