package com.promo.management.system.promomanagement.test.service.promo;

import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_ALREADY_EXISTS;
import static com.promo.management.system.promomanagement.model.enumeration.PromoManagementSystemError.PROMO_CODE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.promo.management.system.promomanagement.model.entity.PromoCode;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.exception.PromoSystemRuntimeValidationException;
import com.promo.management.system.promomanagement.repository.PromoCodeRepository;
import com.promo.management.system.promomanagement.service.promo.PromoCommandService;
import com.promo.management.system.promomanagement.utils.UserSecurityUtils;
import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.UpdatePromoRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PromoCommandServiceTest {

    @Mock
    PromoCodeRepository repo;

    @Captor
    ArgumentCaptor<PromoCode> promoCaptor;

    PromoCommandService service;

    @BeforeEach
    void setUp() {
        service = new PromoCommandService(repo);
    }

    @Test
    void createPromo_alreadyExists() {
        CreatePromoRequestDto req = new CreatePromoRequestDto();
        req.setCode("DUP");
        req.setStatus(PromoCodeStatus.ACTIVE);

        when(repo.existsByCode("DUP")).thenReturn(true);

        assertThatThrownBy(() -> service.createPromo(req))
            .isInstanceOf(PromoSystemRuntimeValidationException.class)
            .hasMessage(PROMO_CODE_ALREADY_EXISTS.getMessage());

        verify(repo, never()).save(any());
    }

    @Test
    void createPromo() {
        CreatePromoRequestDto req = new CreatePromoRequestDto();
        req.setCode("NEW10");
        req.setDiscountValue(10);
        req.setStatus(PromoCodeStatus.ACTIVE);

        when(repo.existsByCode("NEW10")).thenReturn(false);
        when(repo.save(any(PromoCode.class))).thenAnswer(a -> a.getArgument(0));

        try (MockedStatic<UserSecurityUtils> mocked = mockStatic(UserSecurityUtils.class)) {
            mocked.when(UserSecurityUtils::getLoggedInUser).thenReturn("tester");

            service.createPromo(req);

            verify(repo).save(promoCaptor.capture());
            PromoCode saved = promoCaptor.getValue();
            assertThat(saved.getCode()).isEqualTo("NEW10");
            assertThat(saved.getDiscountValue()).isEqualTo(10);
            assertThat(saved.getDiscountPercentage()).isNull();
            assertThat(saved.getStatus()).isEqualTo(PromoCodeStatus.ACTIVE);
            assertThat(saved.getCreatedAt()).isNotNull();
            assertThat(saved.getCreatedBy()).isEqualTo("tester");
        }
    }

    @Test
    void updatePromo_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        UpdatePromoRequestDto req = new UpdatePromoRequestDto();
        req.setStatus(PromoCodeStatus.ACTIVE);

        assertThatThrownBy(() -> service.updatePromo(req, id))
            .isInstanceOf(PromoSystemRuntimeValidationException.class)
            .hasMessage(PROMO_CODE_NOT_FOUND.getMessage());

        verify(repo, never()).save(any());
    }

    @Test
    void updatePromo_throwsAlreadyExists() {
        UUID id = UUID.randomUUID();
        PromoCode existing = PromoCode.builder()
            .id(id)
            .code("OLD")
            .status(PromoCodeStatus.ACTIVE)
            .build();

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.existsByCode("NEW")).thenReturn(true);

        UpdatePromoRequestDto req = new UpdatePromoRequestDto();
        req.setCode("NEW");
        req.setStatus(PromoCodeStatus.ACTIVE);

        assertThatThrownBy(() -> service.updatePromo(req, id))
            .isInstanceOf(PromoSystemRuntimeValidationException.class)
            .hasMessage(PROMO_CODE_ALREADY_EXISTS.getMessage());

        verify(repo, never()).save(any());
    }

    @Test
    void updatePromo() {
        UUID id = UUID.randomUUID();
        PromoCode existing = PromoCode.builder()
            .id(id)
            .code("OLD10")
            .discountValue(null)
            .discountPercentage(15)
            .status(PromoCodeStatus.ACTIVE)
            .createdAt(LocalDateTime.now().minusDays(10))
            .createdBy("creator")
            .build();

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        when(repo.existsByCode("NEW10")).thenReturn(false);
        when(repo.save(any(PromoCode.class))).thenAnswer(a -> a.getArgument(0));

        UpdatePromoRequestDto req = new UpdatePromoRequestDto();
        req.setCode("NEW10");
        req.setDiscountValue(10);
        req.setDiscountPercentage(null);
        req.setUsageLimit(50);
        req.setExpiryDate(LocalDateTime.now().plusDays(30));
        req.setStatus(PromoCodeStatus.ACTIVE);

        try (MockedStatic<UserSecurityUtils> mocked = mockStatic(UserSecurityUtils.class)) {
            mocked.when(UserSecurityUtils::getLoggedInUser).thenReturn("updater");

            service.updatePromo(req, id);

            verify(repo).save(promoCaptor.capture());
            PromoCode saved = promoCaptor.getValue();

            assertThat(saved.getId()).isEqualTo(id);
            assertThat(saved.getCode()).isEqualTo("NEW10");
            assertThat(saved.getUsageLimit()).isEqualTo(50);
            assertThat(saved.getExpiryDate()).isEqualTo(req.getExpiryDate());
            assertThat(saved.getStatus()).isEqualTo(PromoCodeStatus.ACTIVE);

            assertThat(saved.getDiscountValue()).isEqualTo(10);
            assertThat(saved.getDiscountPercentage()).isNull();

            assertThat(saved.getUpdatedAt()).isNotNull();
            assertThat(saved.getUpdatedBy()).isEqualTo("updater");
        }
    }

    @Test
    void deletePromo_throws() {
        UUID id = UUID.randomUUID();
        when(repo.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.deletePromo(id))
            .isInstanceOf(PromoSystemRuntimeValidationException.class)
            .hasMessage(PROMO_CODE_NOT_FOUND.getMessage());

        verify(repo, never()).deleteById(any());
    }

    @Test
    void deletePromo() {
        UUID id = UUID.randomUUID();
        when(repo.existsById(id)).thenReturn(true);

        service.deletePromo(id);

        verify(repo).deleteById(id);
    }

    @Test
    void updatePromoUsage_throws() {
        when(repo.findByCode("NOPE")).thenReturn(null);

        assertThatThrownBy(() -> service.updatePromoUsage("NOPE"))
            .isInstanceOf(PromoSystemRuntimeValidationException.class)
            .hasMessage(PROMO_CODE_NOT_FOUND.getMessage());

        verify(repo, never()).save(any());
    }

    @Test
    void updatePromoUsage() {
        PromoCode entity = PromoCode.builder()
            .id(UUID.randomUUID())
            .code("X")
            .usages(null)
            .build();

        when(repo.findByCode("X")).thenReturn(entity);
        when(repo.save(any(PromoCode.class))).thenAnswer(a -> a.getArgument(0));

        service.updatePromoUsage("X");

        verify(repo).save(promoCaptor.capture());
        assertThat(promoCaptor.getValue().getUsages()).isEqualTo(1);
    }

    @Test
    void updatePromoUsage_increment() {
        PromoCode entity = PromoCode.builder()
            .id(UUID.randomUUID())
            .code("Y")
            .usages(3)
            .build();

        when(repo.findByCode("Y")).thenReturn(entity);
        when(repo.save(any(PromoCode.class))).thenAnswer(a -> a.getArgument(0));

        service.updatePromoUsage("Y");

        verify(repo).save(promoCaptor.capture());
        assertThat(promoCaptor.getValue().getUsages()).isEqualTo(4);
    }

}
