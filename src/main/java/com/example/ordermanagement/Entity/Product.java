package com.example.ordermanagement.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String category;
    private int quantity;

    @OneToMany(mappedBy = "product" ,  cascade = CascadeType.ALL)
    private Set<PriceTo> prices = new HashSet<>();

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL)
    private Set<LineItem> lineItems = new HashSet<>();


    public void addPrice(PriceTo priceTo){
        prices.add(priceTo);
        priceTo.setProduct(this);
    }

    public void addLineItem(LineItem lineItem){
        lineItems.add(lineItem);
        lineItem.setProduct(this);
    }

}
