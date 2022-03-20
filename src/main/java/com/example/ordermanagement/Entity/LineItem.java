package com.example.ordermanagement.Entity;


import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LineItem  {
    @EmbeddedId
    private LineItemKey lineItemKey;

    @MapsId("productID")
    @ManyToOne
    private  Product product;

    @MapsId("orderID")
    @ManyToOne
    private  Order order;

    private int quantity;

}
