package com.promo.management.system.promomanagement.test.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.enumeration.SortBy;
import com.promo.management.system.promomanagement.service.promo.PromoQueryService;
import com.promo.management.system.promomanagement.web.controller.PromoController;
import com.promo.management.system.promomanagement.web.model.dto.request.GetPromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.GetPromoResponseDto;
import com.promo.management.system.promomanagement.web.model.dto.response.PromoDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PromoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PromoControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean PromoQueryService promoQueryService;

    @Test
    void getPromo() throws Exception {
        var now = LocalDateTime.now();
        var start = now.minusDays(7);
        var end = now.plusDays(7);

        var promo = PromoDto.builder()
            .id(UUID.randomUUID())
            .code("SPRING10")
            .discountValue(10)
            .status(PromoCodeStatus.ACTIVE)
            .build();

        var serviceRes = GetPromoResponseDto.builder()
            .promos(List.of(promo))
            .pageNumber(2)
            .pageElements(1)
            .totalPages(3)
            .totalElements(11)
            .build();

        when(promoQueryService.getPromo(any(GetPromoRequestDto.class))).thenReturn(serviceRes);

        mvc.perform(get("/promo")
                .param("code", "SPRING")
                .param("status", "ACTIVE")
                .param("start", start.toString())    // ISO date-time
                .param("end", end.toString())
                .param("sort", "EXPIRATION_DATE")
                .param("direction", "DESC")
                .param("pageNumber", "2")
                .param("pageSize", "5")
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.promos[0].code").value("SPRING10"))
            .andExpect(jsonPath("$.pageNumber").value(2))
            .andExpect(jsonPath("$.pageElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(3))
            .andExpect(jsonPath("$.totalElements").value(11));

        ArgumentCaptor<GetPromoRequestDto> captor = ArgumentCaptor.forClass(GetPromoRequestDto.class);
        verify(promoQueryService).getPromo(captor.capture());
        var dto = captor.getValue();
        assertThat(dto.getCode()).isEqualTo("SPRING");
        assertThat(dto.getStatus()).isEqualTo(PromoCodeStatus.ACTIVE);
        assertThat(dto.getStart()).isEqualTo(start);
        assertThat(dto.getEnd()).isEqualTo(end);
        assertThat(dto.getSort()).isEqualTo(SortBy.EXPIRATION_DATE);
        assertThat(dto.getDirection().name()).isEqualTo("DESC");
        assertThat(dto.getPageNumber()).isEqualTo(2);
        assertThat(dto.getPageSize()).isEqualTo(5);
    }
}
