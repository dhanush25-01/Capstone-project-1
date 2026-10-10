package com.dhanushmart.DhanushMart.service;

import com.dhanushmart.DhanushMart.model.*;
import com.dhanushmart.DhanushMart.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    public CartService(CartRepository cartRepo, CartItemRepository itemRepo,
                       CustomerRepository customerRepo, ProductRepository productRepo){
        this.cartRepo=cartRepo; this.itemRepo=itemRepo;
        this.customerRepo=customerRepo; this.productRepo=productRepo;
    }

    public Cart getOrCreate(Long customerId){
        return cartRepo.findByCustomerId(customerId).orElseGet(() -> {
            Customer c=customerRepo.findById(customerId).orElseThrow();
            Cart cart=new Cart(); cart.setCustomer(c); return cartRepo.save(cart);
        });
    }

    public void add(Long customerId, Long productId, int qty){
        if(qty<1) qty=1;
        Cart cart=getOrCreate(customerId);
        Product p=productRepo.findById(productId).orElseThrow();
        if(p.getStock()<qty) throw new IllegalArgumentException("Not enough stock");
        CartItem item=itemRepo.findByCartIdAndProductId(cart.getId(),productId).orElse(null);
        if(item==null){
            item=new CartItem(); item.setCart(cart); item.setProduct(p); item.setQuantity(qty);
        } else {
            int newQty=item.getQuantity()+qty;
            if(newQty>p.getStock()) throw new IllegalArgumentException("Not enough stock");
            item.setQuantity(newQty);
        }
        itemRepo.save(item);
    }

    public void update(Long customerId, Long itemId, int qty){
        Cart cart=getOrCreate(customerId);
        CartItem item=itemRepo.findById(itemId).orElseThrow();
        if(!item.getCart().getId().equals(cart.getId())) throw new IllegalArgumentException("Invalid cart item");
        if(qty<=0) itemRepo.delete(item);
        else if(qty>item.getProduct().getStock()) throw new IllegalArgumentException("Not enough stock");
        else {item.setQuantity(qty); itemRepo.save(item);}
    }

    public void remove(Long customerId, Long itemId){
        Cart cart=getOrCreate(customerId);
        CartItem item=itemRepo.findById(itemId).orElseThrow();
        if(item.getCart().getId().equals(cart.getId())) itemRepo.delete(item);
    }

    public double total(Cart cart){
        return cart.getItems().stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}
