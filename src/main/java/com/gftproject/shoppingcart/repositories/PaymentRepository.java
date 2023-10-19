package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
