package com.promo.management.system.promomanagement.service.promo;

import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_ALREADY_EXISTS;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_NOT_FOUND;
import static com.promo.management.system.promomanagement.utils.UserSecurityUtils.getLoggedInUser;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.entity.PromoCode;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeValidationException;
import com.promo.management.system.promomanagement.repository.PromoCodeRepository;
import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.UpdatePromoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromoCommandService {

    private final PromoCodeRepository promoCodeRepository;

    @Transactional
    public void createPromo(CreatePromoRequestDto requestDto) {
        if (promoCodeRepository.existsByCode(requestDto.getCode())) {
            throw new PromoSystemRuntimeValidationException(PROMO_CODE_ALREADY_EXISTS);
        }
        promoCodeRepository.save(toPromoCode(requestDto));
    }

    @Transactional
    public void updatePromo(UpdatePromoRequestDto requestDto, UUID id) {
        PromoCode promoCode = promoCodeRepository.findById(id)
            .orElseThrow(() -> new PromoSystemRuntimeValidationException(PROMO_CODE_NOT_FOUND));

        if (hasText(requestDto.getCode()) && !requestDto.getCode().equals(promoCode.getCode()) && promoCodeRepository.existsByCode(requestDto.getCode())) {
            throw new PromoSystemRuntimeValidationException(PROMO_CODE_ALREADY_EXISTS);
        }

        promoCodeRepository.save(toPromoCode(requestDto, promoCode));
    }

    @Transactional
    public void deletePromo(UUID id) {
        if (!promoCodeRepository.existsById(id)) {
            throw new PromoSystemRuntimeValidationException(PROMO_CODE_NOT_FOUND);
        }

        promoCodeRepository.deleteById(id);
    }

    @Transactional
    public void updatePromoUsage(String code) {
        PromoCode promoCode = promoCodeRepository.findByCode(code);

        if (isNull(promoCode)) {
            throw new PromoSystemRuntimeValidationException(PROMO_CODE_NOT_FOUND);
        }

        int usages = Optional.ofNullable(promoCode.getUsages()).orElse(0);
        promoCode.setUsages(usages + 1);
        promoCodeRepository.save(promoCode);
    }

    private static PromoCode toPromoCode(CreatePromoRequestDto requestDto) {
        return PromoCode.builder()
            .code(requestDto.getCode())
            .discountValue(requestDto.getDiscountValue())
            .discountPercentage(requestDto.getDiscountPercentage())
            .expiryDate(requestDto.getExpiryDate())
            .usageLimit(requestDto.getUsageLimit())
            .status(requestDto.getStatus())
            .createdAt(LocalDateTime.now())
            .createdBy(getLoggedInUser())
            .build();
    }

    private static PromoCode toPromoCode(UpdatePromoRequestDto requestDto, PromoCode promoCode) {
        resolveDiscount(requestDto, promoCode);

        return promoCode.toBuilder()
            .code(requestDto.getCode())
            .discountValue(requestDto.getDiscountValue())
            .discountPercentage(requestDto.getDiscountPercentage())
            .expiryDate(requestDto.getExpiryDate())
            .usageLimit(requestDto.getUsageLimit())
            .status(requestDto.getStatus())
            .updatedAt(LocalDateTime.now())
            .updatedBy(getLoggedInUser())
            .build();
    }

    private static void resolveDiscount(UpdatePromoRequestDto requestDto, PromoCode promoCode) {
        if (nonNull(requestDto.getDiscountValue()) && nonNull(promoCode.getDiscountPercentage())) {
            promoCode.setDiscountPercentage(null);
            promoCode.setDiscountValue(requestDto.getDiscountValue());
        } else if (nonNull(requestDto.getDiscountValue()) && nonNull(promoCode.getDiscountValue())) {
            promoCode.setDiscountValue(null);
            promoCode.setDiscountPercentage(requestDto.getDiscountPercentage());
        }
    }

}
