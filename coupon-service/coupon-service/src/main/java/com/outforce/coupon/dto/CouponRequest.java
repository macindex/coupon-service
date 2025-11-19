package com.outforce.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class CouponRequest {

    @NotBlank(message = "Code is mandatory")
    @Size(min = 1, max = 50, message = "Code must be between 1 and 50 characters")
    private String code;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Discount value is mandatory")
    @DecimalMin(value = "0.5", message = "Discount value must be at least 0.5")
    private Double discountValue;

    @NotNull(message = "Expiration date is mandatory")
    @Future(message = "Expiration date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    private Boolean published = false;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }

    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    public Boolean getPublished() { return published; }
    public void setPublished(Boolean published) { this.published = published; }
}
