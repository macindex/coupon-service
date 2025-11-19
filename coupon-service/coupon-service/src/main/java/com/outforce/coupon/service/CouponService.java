package com.outforce.coupon.service;

import com.outforce.coupon.dto.CouponRequest;
import com.outforce.coupon.exception.BusinessException;
import com.outforce.coupon.model.Coupon;
import com.outforce.coupon.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public Coupon createCoupon(CouponRequest request) {
        validateCreateRequest(request);

        String cleanedCode = cleanAndFormatCode(request.getCode());

        if (couponRepository.existsByCodeAndDeletedFalse(cleanedCode)) {
            // Retorna 409 Conflict em vez de 400 Bad Request
            throw new BusinessException("Coupon with code " + cleanedCode + " already exists", HttpStatus.CONFLICT);
        }

        Coupon coupon = new Coupon(
                cleanedCode,
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate(),
                request.getPublished()
        );

        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllActiveCoupons() {
        return couponRepository.findByDeletedFalse();
    }

    public Optional<Coupon> getCouponById(Long id) {
        return couponRepository.findByIdAndDeletedFalse(id);
    }

    public Optional<Coupon> getCouponByCode(String code) {
        String cleanedCode = cleanAndFormatCode(code);
        return couponRepository.findByCodeAndDeletedFalse(cleanedCode);
    }

    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("Coupon not found or already deleted"));

        if (coupon.getDeleted()) {
            throw new BusinessException("Coupon is already deleted");
        }

        int updated = couponRepository.softDelete(id);
        if (updated == 0) {
            throw new BusinessException("Failed to delete coupon");
        }
    }

    private void validateCreateRequest(CouponRequest request) {
        if (request.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Expiration date cannot be in the past");
        }

        if (request.getDiscountValue() < 0.5) {
            throw new BusinessException("Discount value must be at least 0.5");
        }
    }

    private String cleanAndFormatCode(String code) {
        String cleaned = code.replaceAll("[^a-zA-Z0-9]", "").trim().toUpperCase(); // Usar UPPERCASE para consistência

        if (cleaned.length() > 6) {
            // Opção: Manter 4 caracteres do início e 2 do fim para maior chance de unicidade
            return cleaned.substring(0, 4) + cleaned.substring(cleaned.length() - 2);
        } else if (cleaned.length() < 6) {
            // Adicionar o padding à direita com '0' se for menor que 6 (Se esta for a regra desejada)
            return String.format("%-6s", cleaned).replace(' ', '0');
        }

        return cleaned;
    }
}
