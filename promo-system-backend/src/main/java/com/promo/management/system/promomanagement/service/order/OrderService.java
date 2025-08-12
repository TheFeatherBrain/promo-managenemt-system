package com.promo.management.system.promomanagement.service.order;

import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_HAS_EXCEED_ITS_USAGE_LIMIT;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_IS_EXPIRED;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_IS_INVALID;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_NOT_FOUND;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

import com.promo.management.system.promomanagement.model.entity.Order;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeValidationException;
import com.promo.management.system.promomanagement.repository.OrderRepository;
import com.promo.management.system.promomanagement.service.promo.PromoCommandService;
import com.promo.management.system.promomanagement.service.promo.PromoQueryService;
import com.promo.management.system.promomanagement.utils.UserSecurityUtils;
import com.promo.management.system.promomanagement.web.model.dto.request.CreateOrderRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.ValidatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.PromoDto;
import com.promo.management.system.promomanagement.web.model.dto.response.ValidatePromoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PromoQueryService promoQueryService;
    private final PromoCommandService promoCommandService;
    private final OrderRepository orderRepository;

    private static final Map<PromoCodeStatus, Function<PromoDto, ValidatePromoResponseDto>> PROMO_STATUS_MAP = Map.of(
        PromoCodeStatus.ACTIVE, OrderService::isActive,
        PromoCodeStatus.EXPIRED, OrderService::isExpired,
        PromoCodeStatus.DISABLED, OrderService::isDisabled
    );

    @Transactional
    public void createOrder(CreateOrderRequestDto requestDto) {
        boolean codeExists = hasText(requestDto.getCode());
        if (codeExists) {
            ValidatePromoResponseDto validatePromoResponseDto = validatePromo(requestDto.getCode());

            if (!validatePromoResponseDto.isValid()) {
                throw new PromoSystemRuntimeValidationException(PROMO_CODE_IS_INVALID);
            }
        }

        orderRepository.save(toOrder(requestDto));

        if (codeExists) {
            promoCommandService.updatePromoUsage(requestDto.getCode());
        }
    }

    @Transactional(readOnly = true)
    public ValidatePromoResponseDto validatePromo(ValidatePromoRequestDto requestDto) {
        return validatePromo(requestDto.getCode());
    }

    private ValidatePromoResponseDto validatePromo(String code) {
        PromoDto promoDto = promoQueryService.findPromoByCode(code);

        if (isNull(promoDto.getId())) {
            return isDisabled(promoDto);
        }

        if (nonNull(promoDto.getUsageLimit()) && nonNull(promoDto.getUsages())
            && promoDto.getUsageLimit() <= promoDto.getUsages()) {
            return ValidatePromoResponseDto.builder()
                .valid(false)
                .message(PROMO_CODE_HAS_EXCEED_ITS_USAGE_LIMIT.getMessage())
                .build();
        }

        return PROMO_STATUS_MAP.get(promoDto.getStatus()).apply(promoDto);
    }

    private static ValidatePromoResponseDto isActive(PromoDto promoDto) {
        if (nonNull(promoDto.getExpiryDate()) && promoDto.getExpiryDate().isBefore(LocalDateTime.now())) {
            return isExpired(promoDto);
        }

        return ValidatePromoResponseDto.builder()
            .valid(true)
            .message("Promo code is valid.")
            .build();
    }

    private static ValidatePromoResponseDto isExpired(PromoDto promoDto) {
        return ValidatePromoResponseDto.builder()
            .valid(false)
            .message(PROMO_CODE_IS_EXPIRED.getMessage())
            .build();
    }

    private static ValidatePromoResponseDto isDisabled(PromoDto promoDto) {
        return ValidatePromoResponseDto.builder()
            .valid(false)
            .message(PROMO_CODE_NOT_FOUND.getMessage())
            .build();
    }

    private static Order toOrder(CreateOrderRequestDto requestDto) {
        return Order.builder()
            .promoCode(requestDto.getCode())
            .createdAt(LocalDateTime.now())
            .createdBy(UserSecurityUtils.getLoggedInUser())
            .build();
    }

}
