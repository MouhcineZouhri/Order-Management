package com.example.ordermanagement.Entity;


import com.example.ordermanagement.Enums.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = " created_at")
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    private AppUser user;

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL)
    private Set<LineItem> lineItems = new HashSet<>();


    public void addLineItem(LineItem lineItem){
        lineItems.add(lineItem);
        lineItem.setOrder(this);
    }

}
