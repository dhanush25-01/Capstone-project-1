package com.dhanushmart.DhanushMart.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String name;
    @Column(unique=true,nullable=false) private String username;
    @Column(nullable=false) private String password;
    private String email;
    private String phone;

    @Column(nullable=false)
    private String role = "USER";

    // PENDING until an admin approves the registration.
    @Column(nullable=false)
    private String status = "PENDING";

    public User() {}

    public Long getId(){return id;} public void setId(Long x){id=x;}
    public String getName(){return name;} public void setName(String x){name=x;}
    public String getUsername(){return username;} public void setUsername(String x){username=x;}
    public String getPassword(){return password;} public void setPassword(String x){password=x;}
    public String getEmail(){return email;} public void setEmail(String x){email=x;}
    public String getPhone(){return phone;} public void setPhone(String x){phone=x;}
    public String getRole(){return role;} public void setRole(String x){role=x;}
    public String getStatus(){return status;} public void setStatus(String x){status=x;}
}
