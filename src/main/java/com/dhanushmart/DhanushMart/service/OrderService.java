package com.dhanushmart.DhanushMart.service;

import com.dhanushmart.DhanushMart.model.*;
import com.dhanushmart.DhanushMart.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final CartService cartService;
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public OrderService(OrderRepository orderRepo, OrderItemRepository itemRepo,
                        CartService cartService, CartRepository cartRepo,
                        ProductRepository productRepo){
        this.orderRepo=orderRepo; this.itemRepo=itemRepo;
        this.cartService=cartService; this.cartRepo=cartRepo; this.productRepo=productRepo;
    }

    @Transactional
    public Order checkout(Customer customer, String address){
        Cart cart=cartService.getOrCreate(customer.getId());
        if(cart.getItems().isEmpty()) throw new IllegalArgumentException("Cart is empty");

        Order order=new Order();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setStatus("PLACED");

        double total=0;
        for(CartItem ci: cart.getItems()){
            Product p=productRepo.findById(ci.getProduct().getId()).orElseThrow();
            if(p.getStock()<ci.getQuantity()) throw new IllegalArgumentException("Insufficient stock for "+p.getName());
            p.setStock(p.getStock()-ci.getQuantity());
            productRepo.save(p);

            OrderItem oi=new OrderItem();
            oi.setOrder(order); oi.setProduct(p);
            oi.setQuantity(ci.getQuantity()); oi.setPrice(p.getPrice());
            order.getItems().add(oi);
            total += p.getPrice()*ci.getQuantity();
        }
        order.setTotalAmount(total);
        Order saved=orderRepo.save(order);
        cart.getItems().clear();
        cartRepo.save(cart);
        return saved;
    }
}
