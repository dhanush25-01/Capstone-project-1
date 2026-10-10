package com.dhanushmart.DhanushMart.model;

import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String name;
    @Column(columnDefinition="TEXT") private String description;
    private double price;
    private String size;
    private String color;
    private int stock;
    private String image;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    // Nullable so existing/admin-created products remain valid.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    public Product() {}
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
    public double getPrice(){return price;}
    public void setPrice(double price){this.price=price;}
    public String getSize(){return size;}
    public void setSize(String size){this.size=size;}
    public String getColor(){return color;}
    public void setColor(String color){this.color=color;}
    public int getStock(){return stock;}
    public void setStock(int stock){this.stock=stock;}
    public String getImage(){return image;}
    public void setImage(String image){this.image=image;}
    public Category getCategory(){return category;}
    public void setCategory(Category category){this.category=category;}
    public User getSeller(){return seller;}
    public void setSeller(User seller){this.seller=seller;}
}
