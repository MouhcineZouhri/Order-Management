package com.example.ordermanagement.Entity;


import com.example.ordermanagement.Enums.DeliveryStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @ManyToOne
    private AppUser user;

    @OneToOne
    private Order order;
}
