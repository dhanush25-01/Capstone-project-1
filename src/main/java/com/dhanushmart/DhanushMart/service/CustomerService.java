package com.dhanushmart.DhanushMart.service;

import com.dhanushmart.DhanushMart.model.Customer;
import com.dhanushmart.DhanushMart.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository repo;
    public CustomerService(CustomerRepository repo){this.repo=repo;}

    public Customer register(Customer c){
        if(repo.findByEmail(c.getEmail()).isPresent())
            throw new IllegalArgumentException("Email already registered");
        return repo.save(c);
    }

    public Customer login(String email,String password){
        return repo.findByEmail(email)
                .filter(c -> c.getPassword().equals(password))
                .orElse(null);
    }
}
