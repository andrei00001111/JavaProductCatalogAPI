package com.example.catalog.repository;

import com.example.catalog.entity.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findByExternalId(String externalId);
}
