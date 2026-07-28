package com.example.order_service.controller;

import com.example.order_service.entity.Order;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService service;

  @GetMapping
  public List<Order> findAll() {

    return service.findAll();

  }

  @GetMapping("/{id}")
  public Order findById(
      @PathVariable Long id) {

    return service.findById(id);

  }

  @PostMapping
  public Order create(
      @RequestBody Order order) {

    return service.createOrder(order);

  }

}