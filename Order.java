package com.dhanushmart.DhanushMart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="orders")
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="customer_id")
    private Customer customer;

    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;
    private String shippingAddress;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void beforeSave(){ if(orderDate==null) orderDate=LocalDateTime.now(); }

    public Order() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Customer getCustomer(){return customer;}
    public void setCustomer(Customer customer){this.customer=customer;}
    public LocalDateTime getOrderDate(){return orderDate;}
    public void setOrderDate(LocalDateTime orderDate){this.orderDate=orderDate;}
    public double getTotalAmount(){return totalAmount;}
    public void setTotalAmount(double totalAmount){this.totalAmount=totalAmount;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
    public String getShippingAddress(){return shippingAddress;}
    public void setShippingAddress(String shippingAddress){this.shippingAddress=shippingAddress;}
    public List<OrderItem> getItems(){return items;}
    public void setItems(List<OrderItem> items){this.items=items;}
}
