package com.outforce.coupon.service;


import com.outforce.coupon.dto.CouponRequest;
import com.outforce.coupon.exception.BusinessException;
import com.outforce.coupon.model.Coupon;
import com.outforce.coupon.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private CouponRequest validRequest;
    private Coupon sampleCoupon;

    @BeforeEach
    void setUp() {
        validRequest = new CouponRequest();
        validRequest.setCode("ABC123");
        validRequest.setDescription("Test Coupon");
        validRequest.setDiscountValue(10.0);
        validRequest.setExpirationDate(LocalDateTime.now().plusDays(30));
        validRequest.setPublished(false);

        sampleCoupon = new Coupon();
        sampleCoupon.setId(1L);
        sampleCoupon.setCode("ABC123");
        sampleCoupon.setDescription("Test Coupon");
        sampleCoupon.setDiscountValue(10.0);
        sampleCoupon.setExpirationDate(LocalDateTime.now().plusDays(30));
        sampleCoupon.setDeleted(false);
    }

    @Test
    void createCoupon_WithValidData_ShouldCreateCoupon() {
        when(couponRepository.existsByCodeAndDeletedFalse(any())).thenReturn(false);
        when(couponRepository.save(any(Coupon.class))).thenReturn(sampleCoupon);

        Coupon result = couponService.createCoupon(validRequest);

        assertNotNull(result);
        assertEquals("ABC123", result.getCode());
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void createCoupon_WithPastExpirationDate_ShouldThrowException() {
        validRequest.setExpirationDate(LocalDateTime.now().minusDays(1));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> couponService.createCoupon(validRequest));

        assertEquals("Expiration date cannot be in the past", exception.getMessage());
    }

    @Test
    void createCoupon_WithDiscountValueBelowMinimum_ShouldThrowException() {
        validRequest.setDiscountValue(0.4);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> couponService.createCoupon(validRequest));

        assertEquals("Discount value must be at least 0.5", exception.getMessage());
    }

    @Test
    void createCoupon_WithSpecialCharactersInCode_ShouldCleanCode() {
        validRequest.setCode("AB@C#1$23!");
        when(couponRepository.existsByCodeAndDeletedFalse("ABC123")).thenReturn(false);
        when(couponRepository.save(any(Coupon.class))).thenReturn(sampleCoupon);

        Coupon result = couponService.createCoupon(validRequest);

        assertEquals("ABC123", result.getCode());
    }

    @Test
    void createCoupon_WithLongCode_ShouldTruncateTo6Characters() {
        validRequest.setCode("ABCDEFGHIJ");
        when(couponRepository.existsByCodeAndDeletedFalse("ABCDEF")).thenReturn(false);
        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> {
            Coupon coupon = invocation.getArgument(0);
            assertEquals("ABCDEF", coupon.getCode());
            return sampleCoupon;
        });

        couponService.createCoupon(validRequest);
    }

    @Test
    void createCoupon_WithShortCode_ShouldPadTo6Characters() {
        validRequest.setCode("AB1");
        when(couponRepository.existsByCodeAndDeletedFalse("AB1000")).thenReturn(false);
        when(couponRepository.save(any(Coupon.class))).thenAnswer(invocation -> {
            Coupon coupon = invocation.getArgument(0);
            assertEquals("AB1000", coupon.getCode());
            return sampleCoupon;
        });

        couponService.createCoupon(validRequest);
    }

    @Test
    void deleteCoupon_WithValidId_ShouldSoftDelete() {
        when(couponRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(sampleCoupon));
        when(couponRepository.softDelete(1L)).thenReturn(1);

        couponService.deleteCoupon(1L);

        verify(couponRepository, times(1)).softDelete(1L);
    }

    @Test
    void deleteCoupon_WithAlreadyDeletedCoupon_ShouldThrowException() {
        sampleCoupon.setDeleted(true);
        when(couponRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> couponService.deleteCoupon(1L));

        assertEquals("Coupon not found or already deleted", exception.getMessage());
    }

    @Test
    void createCoupon_WithDuplicateCode_ShouldThrowException() {
        when(couponRepository.existsByCodeAndDeletedFalse("ABC123")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> couponService.createCoupon(validRequest));

        assertEquals("Coupon with code ABC123 already exists", exception.getMessage());
    }
}
