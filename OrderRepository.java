package com.dhanushmart.DhanushMart.repository;
import com.dhanushmart.DhanushMart.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);
}