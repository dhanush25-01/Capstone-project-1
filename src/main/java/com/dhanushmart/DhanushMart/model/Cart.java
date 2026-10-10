package com.dhanushmart.DhanushMart.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="carts")
public class Cart {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="customer_id", unique=true)
    private Customer customer;

    @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Customer getCustomer(){return customer;}
    public void setCustomer(Customer customer){this.customer=customer;}
    public List<CartItem> getItems(){return items;}
    public void setItems(List<CartItem> items){this.items=items;}
}
