package com.dhanushmart.DhanushMart.repository;
import com.dhanushmart.DhanushMart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderItemRepository extends JpaRepository<OrderItem,Long>{}