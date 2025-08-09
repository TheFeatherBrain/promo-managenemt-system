package com.promo.management.system.promomanagement;

import java.time.LocalDateTime;
import java.util.Optional;

import com.promo.management.system.promomanagement.model.dto.CreatePromoCodeRequestDto;
import com.promo.management.system.promomanagement.model.entity.PromoCode;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeException;
import com.promo.management.system.promomanagement.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public void createPromoCode(CreatePromoCodeRequestDto requestDto) {
        if (promoCodeRepository.existsByCode(requestDto.getCode())) {
            throw new PromoSystemRuntimeException(PromoManagementSystemError.PROMO_CODE_ALREADY_EXISTS);
        }
        promoCodeRepository.save(toPromoCode(requestDto));
    }

    public PromoCode getPromoCodeByCode(String code) {
        return promoCodeRepository.findByCode(code).orElseThrow(
            () -> new PromoSystemRuntimeException(PromoManagementSystemError.PROMO_CODE_NOT_FOUND));
    }

    private static PromoCode toPromoCode(CreatePromoCodeRequestDto requestDto) {
        return PromoCode.builder()
            .code(requestDto.getCode())
            .discountValue(requestDto.getDiscountValue())
            .discountPercentage(requestDto.getDiscountPercentage())
            .expiryDate(requestDto.getExpiryDate())
            .usageLimit(requestDto.getUsageLimit())
            .status(Optional.ofNullable(requestDto.getStatus()).orElse(PromoCodeStatus.ACTIVE))
            .createdAt(LocalDateTime.now())
            .createdBy("TEST")
            .build();
    }

}
