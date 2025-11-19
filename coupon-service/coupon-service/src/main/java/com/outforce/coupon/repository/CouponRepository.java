package com.outforce.coupon.repository;

import com.outforce.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCodeAndDeletedFalse(String code);

    List<Coupon> findByDeletedFalse();

    Optional<Coupon> findByIdAndDeletedFalse(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Coupon c SET c.deleted = true WHERE c.id = :id AND c.deleted = false")
    int softDelete(@Param("id") Long id);

    boolean existsByCodeAndDeletedFalse(String code);
}
