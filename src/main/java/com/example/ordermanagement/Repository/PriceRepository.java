package com.example.ordermanagement.Repository;

import com.example.ordermanagement.Entity.PriceTo;
import com.example.ordermanagement.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<PriceTo , Long> {
    public PriceTo findPriceToByProductAndLast(Product product , boolean last);
}
