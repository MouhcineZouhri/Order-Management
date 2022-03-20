package com.example.ordermanagement.Repository;


import com.example.ordermanagement.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment , Long> {
}
