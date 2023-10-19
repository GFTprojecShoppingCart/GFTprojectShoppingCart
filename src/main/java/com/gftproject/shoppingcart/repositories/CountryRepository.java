package com.gftproject.shoppingcart.repositories;

import com.gftproject.shoppingcart.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
