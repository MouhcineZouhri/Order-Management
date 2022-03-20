package com.example.ordermanagement.Entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LineItemKey  implements Serializable {

    @Column(name = "product_id")
    private Long productID;

    @Column(name = "order_id")
    private Long orderID;
}
