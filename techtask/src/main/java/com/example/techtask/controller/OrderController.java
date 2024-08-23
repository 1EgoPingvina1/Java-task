package com.example.techtask.controller;

import com.example.techtask.model.Order;
import com.example.techtask.service.OrderService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.service.spi.InjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Attention! Only DI and service interaction applicable in this class. Each controller method
 * should only contain a call to the corresponding service method
 */
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

  // DI here
  private OrderService _orderService;
  public OrderController(OrderService orderService) {
    _orderService = orderService;
  }

  @GetMapping("desired-order")
  public Order findOrder() {
    return _orderService.findOrder();
  }

  @GetMapping("desired-orders")
  public List<Order> findOrders() {
    return _orderService.findOrders();
  }
}
