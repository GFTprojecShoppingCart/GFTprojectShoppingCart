package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

}
