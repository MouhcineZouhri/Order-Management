package com.example.ordermanagement.Entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    @Column(name = "payment_description")
    private String paymentDescription;

    private String type;

    @Column(name = "payment_date")
    private Date date;

    @ManyToOne
    private AppUser user;

    @OneToOne
    private Order order;
}
