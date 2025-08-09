package com.promo.management.system.promomanagement.repository;

import java.util.Optional;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID> {

    boolean existsByCode(String code);

    Optional<PromoCode> findByCode(String code);

}
