package com.example.ordermanagement.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PriceTo {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal price;
    private Date dateFrom ;
    private Boolean last;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Product product;
}
