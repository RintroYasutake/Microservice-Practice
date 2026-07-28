package com.example.order_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

  private final RestTemplate restTemplate;

  public ProductClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public boolean existsProduct(Long productId) {

    String url = "http://product-service:8081/products/" + productId;

    try {
      restTemplate.getForObject(
          url,
          Object.class);
      return true;
    } catch (Exception e) {
      e.printStackTrace();

      return false;
    }
  }
}