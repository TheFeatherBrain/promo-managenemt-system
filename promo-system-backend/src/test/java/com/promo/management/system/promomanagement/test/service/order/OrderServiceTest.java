package com.promo.management.system.promomanagement.test.service.order;

import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_HAS_EXCEED_ITS_USAGE_LIMIT;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_IS_EXPIRED;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_IS_INVALID;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeValidationException;
import com.promo.management.system.promomanagement.repository.OrderRepository;
import com.promo.management.system.promomanagement.service.order.OrderService;
import com.promo.management.system.promomanagement.service.promo.PromoCommandService;
import com.promo.management.system.promomanagement.service.promo.PromoQueryService;
import com.promo.management.system.promomanagement.web.model.dto.request.CreateOrderRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.ValidatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.PromoDto;
import com.promo.management.system.promomanagement.web.model.dto.response.ValidatePromoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    PromoQueryService promoQueryService;

    @Mock
    PromoCommandService promoCommandService;

    @Mock
    OrderRepository orderRepository;

    OrderService service;

    @BeforeEach
    void setUp() {
        service = new OrderService(promoQueryService, promoCommandService, orderRepository);
    }

    @Test
    void createOrder_invalidPromo() {
        String code = "BADCODE";
        CreateOrderRequestDto req = new CreateOrderRequestDto();
        req.setCode(code);

        when(promoQueryService.findPromoByCode(code)).thenReturn(PromoDto.builder().id(null).build());

        assertThatThrownBy(() -> service.createOrder(req))
            .isInstanceOf(PromoSystemRuntimeValidationException.class)
            .hasMessage(PROMO_CODE_IS_INVALID.getMessage());

        verify(orderRepository, never()).save(any());
        verify(promoCommandService, never()).updatePromoUsage(anyString());
    }

    @Test
    void validatePromo_notFound() {
        String code = "X";
        when(promoQueryService.findPromoByCode(code)).thenReturn(PromoDto.builder().id(null).build());

        ValidatePromoResponseDto res = callValidate(code);

        assertThat(res.isValid()).isFalse();
        assertThat(res.getMessage()).isEqualTo(PROMO_CODE_NOT_FOUND.getMessage());
    }

    @Test
    void validatePromo_usage() {
        String code = "LIMITED";
        when(promoQueryService.findPromoByCode(code)).thenReturn(
            promo(UUID.randomUUID(), code, PromoCodeStatus.ACTIVE, LocalDateTime.now().plusDays(1), 5, 5)
        );

        ValidatePromoResponseDto res = callValidate(code);

        assertThat(res.isValid()).isFalse();
        assertThat(res.getMessage()).isEqualTo(PROMO_CODE_HAS_EXCEED_ITS_USAGE_LIMIT.getMessage());
    }

    @Test
    void validatePromo_active() {
        String code = "ACTIVE_OK";
        when(promoQueryService.findPromoByCode(code)).thenReturn(
            promo(UUID.randomUUID(), code, PromoCodeStatus.ACTIVE, LocalDateTime.now().plusDays(10), null, null)
        );

        ValidatePromoResponseDto res = callValidate(code);

        assertThat(res.isValid()).isTrue();
        assertThat(res.getMessage()).isEqualTo("Promo code is valid.");
    }

    @Test
    void validatePromo_expired() {
        String code = "ACTIVE_BUT_OLD";
        when(promoQueryService.findPromoByCode(code)).thenReturn(
            promo(UUID.randomUUID(), code, PromoCodeStatus.ACTIVE, LocalDateTime.now().minusSeconds(1), null, null)
        );

        ValidatePromoResponseDto res = callValidate(code);

        assertThat(res.isValid()).isFalse();
        assertThat(res.getMessage()).isEqualTo(PROMO_CODE_IS_EXPIRED.getMessage());
    }

    @Test
    void validatePromo_status_expired() {
        String code = "EXPIRED";
        when(promoQueryService.findPromoByCode(code)).thenReturn(
            promo(UUID.randomUUID(), code, PromoCodeStatus.EXPIRED, LocalDateTime.now().plusDays(5), null, null)
        );

        ValidatePromoResponseDto res = callValidate(code);

        assertThat(res.isValid()).isFalse();
        assertThat(res.getMessage()).isEqualTo(PROMO_CODE_IS_EXPIRED.getMessage());
    }

    @Test
    void validatePromo_disabled() {
        String code = "DISABLED";
        when(promoQueryService.findPromoByCode(code)).thenReturn(
            promo(UUID.randomUUID(), code, PromoCodeStatus.DISABLED, null, null, null)
        );

        ValidatePromoResponseDto res = callValidate(code);

        assertThat(res.isValid()).isFalse();
        assertThat(res.getMessage()).isEqualTo(PROMO_CODE_NOT_FOUND.getMessage());
    }

    @Test
    void validatePromo() {
        String code = "DTO_PATH";
        when(promoQueryService.findPromoByCode(code)).thenReturn(
            promo(UUID.randomUUID(), code, PromoCodeStatus.ACTIVE, null, null, null)
        );

        ValidatePromoRequestDto dto = new ValidatePromoRequestDto();
        dto.setCode(code);

        ValidatePromoResponseDto res = service.validatePromo(dto);

        assertThat(res.isValid()).isTrue();
        assertThat(res.getMessage()).isEqualTo("Promo code is valid.");
    }

    private ValidatePromoResponseDto callValidate(String code) {
        ValidatePromoRequestDto dto = new ValidatePromoRequestDto();
        dto.setCode(code);
        return service.validatePromo(dto);
    }

    private static PromoDto promo(UUID id,
                                  String code,
                                  PromoCodeStatus status,
                                  LocalDateTime expiry,
                                  Integer usageLimit,
                                  Integer usages) {
        return PromoDto.builder()
            .id(id)
            .code(code)
            .status(status)
            .expiryDate(expiry)
            .usageLimit(usageLimit)
            .usages(usages)
            .build();
    }

}
