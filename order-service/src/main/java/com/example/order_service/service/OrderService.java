package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository repository;
  private final ProductClient productClient;

  public List<Order> findAll() {
    return repository.findAll();
  }

  public Order findById(Long id) {

    return repository.findById(id)
        .orElseThrow();

  }

  public Order create(Order order) {

    return repository.save(order);

  }

  public Order createOrder(Order order) {

    boolean exists = productClient.existsProduct(order.getProductId());

    if (!exists) {
      throw new RuntimeException(
          "Product not found");
    }

    return repository.save(order);
  }
}