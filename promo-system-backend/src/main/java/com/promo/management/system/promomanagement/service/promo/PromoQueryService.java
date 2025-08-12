package com.promo.management.system.promomanagement.service.promo;

import static com.promo.management.system.promomanagement.repository.specification.PromoCodeSpecification.hasCode;
import static com.promo.management.system.promomanagement.repository.specification.PromoCodeSpecification.hasEndDate;
import static com.promo.management.system.promomanagement.repository.specification.PromoCodeSpecification.hasStartDate;
import static com.promo.management.system.promomanagement.repository.specification.PromoCodeSpecification.hasStatus;
import static java.util.Objects.isNull;

import java.util.List;

import com.promo.management.system.promomanagement.model.entity.PromoCode;
import com.promo.management.system.promomanagement.repository.PromoCodeRepository;
import com.promo.management.system.promomanagement.web.model.dto.request.GetPromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.GetPromoResponseDto;
import com.promo.management.system.promomanagement.web.model.dto.response.PromoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromoQueryService {

    private final PromoCodeRepository promoCodeRepository;

    @Transactional(readOnly = true)
    public GetPromoResponseDto getPromo(GetPromoRequestDto requestDto) {
        Sort sort = Sort.by(requestDto.getDirection(), requestDto.getSort().getColumn());

        Page<PromoCode> promoCodePage = promoCodeRepository.findAll(toSpecification(requestDto),
            PageRequest.of(requestDto.getPageNumber(), requestDto.getPageSize(), sort));

        List<PromoDto> promos = promoCodePage.getContent().stream()
            .map(PromoQueryService::toPromoDto)
            .toList();

        return GetPromoResponseDto.builder()
            .promos(promos)
            .pageElements(promoCodePage.getNumberOfElements())
            .pageNumber(promoCodePage.getNumber())
            .totalPages(promoCodePage.getTotalPages())
            .totalElements(promoCodePage.getTotalElements())
            .build();
    }

    @Transactional(readOnly = true)
    public PromoDto findPromoByCode(String code) {
        return toPromoDto(promoCodeRepository.findByCode(code));
    }

    private static Specification<PromoCode> toSpecification(GetPromoRequestDto requestDto) {
        return hasCode(requestDto.getCode())
            .and(hasStatus(requestDto.getStatus()))
            .and(hasStartDate(requestDto.getStart()))
            .and(hasEndDate(requestDto.getEnd()));
    }

    private static PromoDto toPromoDto(PromoCode promoCode) {
        if (isNull(promoCode)) {
            return PromoDto.builder().build();
        }

        return PromoDto.builder()
            .id(promoCode.getId())
            .code(promoCode.getCode())
            .discountValue(promoCode.getDiscountValue())
            .discountPercentage(promoCode.getDiscountPercentage())
            .expiryDate(promoCode.getExpiryDate())
            .usageLimit(promoCode.getUsageLimit())
            .usages(promoCode.getUsages())
            .status(promoCode.getStatus())
            .createdAt(promoCode.getCreatedAt())
            .createdBy(promoCode.getCreatedBy())
            .updatedAt(promoCode.getUpdatedAt())
            .updatedBy(promoCode.getUpdatedBy())
            .build();
    }

}
