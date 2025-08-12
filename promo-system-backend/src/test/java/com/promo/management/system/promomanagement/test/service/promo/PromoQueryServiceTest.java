package com.promo.management.system.promomanagement.test.service.promo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.entity.PromoCode;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.enumeration.SortBy;
import com.promo.management.system.promomanagement.repository.PromoCodeRepository;
import com.promo.management.system.promomanagement.service.promo.PromoQueryService;
import com.promo.management.system.promomanagement.web.model.dto.request.GetPromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.GetPromoResponseDto;
import com.promo.management.system.promomanagement.web.model.dto.response.PromoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class PromoQueryServiceTest {

    @Mock
    PromoCodeRepository promoCodeRepository;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

    PromoQueryService service;

    @BeforeEach
    void setUp() {
        service = new PromoQueryService(promoCodeRepository);
    }

    @Test
    void getPromo() {
        GetPromoRequestDto req = new GetPromoRequestDto();
        req.setPageNumber(2);
        req.setPageSize(5);
        req.setSort(SortBy.EXPIRATION_DATE);
        req.setDirection(Sort.Direction.DESC);
        req.setCode("SPRING");

        PromoCode e1 = mockPromoEntity(
            UUID.randomUUID(), "SPRING10", 10, null,
            LocalDateTime.now().plusDays(5), 100, 7, PromoCodeStatus.ACTIVE,
            LocalDateTime.now().minusDays(1), "alice",
            LocalDateTime.now(), "bob"
        );
        PromoCode e2 = mockPromoEntity(
            UUID.randomUUID(), "WINTER20", null, 20,
            LocalDateTime.now().plusDays(10), null, null, PromoCodeStatus.DISABLED,
            LocalDateTime.now().minusDays(2), "carol",
            null, null
        );

        PageImpl<PromoCode> page = new PageImpl<>(
            List.of(e1, e2),
            PageRequest.of(2, 5, Sort.by(Sort.Direction.DESC, SortBy.EXPIRATION_DATE.getColumn())),
            12
        );

        when(promoCodeRepository.findAll(ArgumentMatchers.<Specification<PromoCode>>any(), any(PageRequest.class))).thenReturn(page);

        GetPromoResponseDto res = service.getPromo(req);

        verify(promoCodeRepository).findAll(ArgumentMatchers.<Specification<PromoCode>>any(), pageRequestCaptor.capture());
        PageRequest pr = pageRequestCaptor.getValue();
        assertThat(pr.getPageNumber()).isEqualTo(2);
        assertThat(pr.getPageSize()).isEqualTo(5);
        assertThat(pr.getSort().getOrderFor("expiryDate")).isNotNull();
        assertThat(pr.getSort().getOrderFor("expiryDate").getDirection()).isEqualTo(Sort.Direction.DESC);

        assertThat(res.getPageNumber()).isEqualTo(2);
        assertThat(res.getPageElements()).isEqualTo(2);
        assertThat(res.getTotalElements()).isEqualTo(12);
        assertThat(res.getTotalPages()).isEqualTo(3); // 12 elements, size 5 -> 3 pages

        assertThat(res.getPromos()).hasSize(2);

        PromoDto d1 = res.getPromos().getFirst();
        assertThat(d1.getCode()).isEqualTo("SPRING10");
        assertThat(d1.getDiscountValue()).isEqualTo(10);
        assertThat(d1.getDiscountPercentage()).isNull();
        assertThat(d1.getExpiryDate()).isEqualTo(e1.getExpiryDate());
        assertThat(d1.getUsageLimit()).isEqualTo(100);
        assertThat(d1.getUsages()).isEqualTo(7);
        assertThat(d1.getStatus()).isEqualTo(PromoCodeStatus.ACTIVE);
        assertThat(d1.getCreatedAt()).isEqualTo(e1.getCreatedAt());
        assertThat(d1.getCreatedBy()).isEqualTo("alice");
        assertThat(d1.getUpdatedAt()).isEqualTo(e1.getUpdatedAt());
        assertThat(d1.getUpdatedBy()).isEqualTo("bob");

        PromoDto d2 = res.getPromos().get(1);
        assertThat(d2.getCode()).isEqualTo("WINTER20");
        assertThat(d2.getDiscountValue()).isNull();
        assertThat(d2.getDiscountPercentage()).isEqualTo(20);
        assertThat(d2.getStatus()).isEqualTo(PromoCodeStatus.DISABLED);
        assertThat(d2.getUpdatedAt()).isNull();
        assertThat(d2.getUpdatedBy()).isNull();
    }

    @Test
    void findPromoByCode_returnsEmptyDto() {
        when(promoCodeRepository.findByCode("NOPE")).thenReturn(null);

        PromoDto dto = service.findPromoByCode("NOPE");

        assertThat(dto.getId()).isNull();
        assertThat(dto.getCode()).isNull();
        assertThat(dto.getStatus()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
        assertThat(dto.getUpdatedAt()).isNull();
    }

    @Test
    void findPromoByCode_mapsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime exp = LocalDateTime.now().plusDays(30);
        LocalDateTime createdAt = LocalDateTime.now().minusDays(5);
        LocalDateTime updatedAt = LocalDateTime.now().minusDays(1);

        PromoCode entity = mockPromoEntity(
            id, "SUMMER15", 15, null, exp,
            50, 12, PromoCodeStatus.EXPIRED,
            createdAt, "dave", updatedAt, "erin"
        );

        when(promoCodeRepository.findByCode("SUMMER15")).thenReturn(entity);

        PromoDto dto = service.findPromoByCode("SUMMER15");

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getCode()).isEqualTo("SUMMER15");
        assertThat(dto.getDiscountValue()).isEqualTo(15);
        assertThat(dto.getDiscountPercentage()).isNull();
        assertThat(dto.getExpiryDate()).isEqualTo(exp);
        assertThat(dto.getUsageLimit()).isEqualTo(50);
        assertThat(dto.getUsages()).isEqualTo(12);
        assertThat(dto.getStatus()).isEqualTo(PromoCodeStatus.EXPIRED);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getCreatedBy()).isEqualTo("dave");
        assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(dto.getUpdatedBy()).isEqualTo("erin");
    }

    private static PromoCode mockPromoEntity(
        UUID id,
        String code,
        Integer discountValue,
        Integer discountPercentage,
        LocalDateTime expiryDate,
        Integer usageLimit,
        Integer usages,
        PromoCodeStatus status,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
    ) {
        PromoCode m = mock(PromoCode.class);
        when(m.getId()).thenReturn(id);
        when(m.getCode()).thenReturn(code);
        when(m.getDiscountValue()).thenReturn(discountValue);
        when(m.getDiscountPercentage()).thenReturn(discountPercentage);
        when(m.getExpiryDate()).thenReturn(expiryDate);
        when(m.getUsageLimit()).thenReturn(usageLimit);
        when(m.getUsages()).thenReturn(usages);
        when(m.getStatus()).thenReturn(status);
        when(m.getCreatedAt()).thenReturn(createdAt);
        when(m.getCreatedBy()).thenReturn(createdBy);
        when(m.getUpdatedAt()).thenReturn(updatedAt);
        when(m.getUpdatedBy()).thenReturn(updatedBy);
        return m;
    }

}
