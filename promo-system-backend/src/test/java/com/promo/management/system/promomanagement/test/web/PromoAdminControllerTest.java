package com.promo.management.system.promomanagement.test.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.service.promo.PromoCommandService;
import com.promo.management.system.promomanagement.web.controller.PromoAdminController;
import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.UpdatePromoRequestDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PromoAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class PromoAdminControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean PromoCommandService promoCommandService;

    @Test
    void createPromo() throws Exception {
        CreatePromoRequestDto req = new CreatePromoRequestDto();
        req.setCode("NEW10");
        req.setDiscountValue(10);
        req.setStatus(PromoCodeStatus.ACTIVE);

        mvc.perform(post("/admin/promo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));

        ArgumentCaptor<CreatePromoRequestDto> captor = ArgumentCaptor.forClass(CreatePromoRequestDto.class);
        verify(promoCommandService).createPromo(captor.capture());
        assertThat(captor.getValue().getCode()).isEqualTo("NEW10");
        assertThat(captor.getValue().getStatus()).isEqualTo(PromoCodeStatus.ACTIVE);
    }

    @Test
    void createPromo_missingCode() throws Exception {
        String body = """
          {"status":"ACTIVE"}
        """;

        mvc.perform(post("/admin/promo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(promoCommandService);
    }

    @Test
    void createPromo_missingStatus() throws Exception {
        String body = """
          {"code":"X10"}
        """;

        mvc.perform(post("/admin/promo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(promoCommandService);
    }

    @Test
    void updatePromo_ok() throws Exception {
        UUID id = UUID.randomUUID();

        UpdatePromoRequestDto req = new UpdatePromoRequestDto();
        req.setCode("UPD20");
        req.setDiscountPercentage(20);
        req.setStatus(PromoCodeStatus.ACTIVE);

        mvc.perform(put("/admin/promo/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isAccepted())
            .andExpect(content().string(""));

        ArgumentCaptor<UpdatePromoRequestDto> dtoCap = ArgumentCaptor.forClass(UpdatePromoRequestDto.class);
        ArgumentCaptor<UUID> idCap = ArgumentCaptor.forClass(UUID.class);

        verify(promoCommandService).updatePromo(dtoCap.capture(), idCap.capture());
        assertThat(idCap.getValue()).isEqualTo(id);
        assertThat(dtoCap.getValue().getCode()).isEqualTo("UPD20");
        assertThat(dtoCap.getValue().getStatus()).isEqualTo(PromoCodeStatus.ACTIVE);
    }

    @Test
    void updatePromo_missingStatus() throws Exception {
        UUID id = UUID.randomUUID();
        String body = """
          {"code":"ANY"}
        """;

        mvc.perform(put("/admin/promo/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());

        verify(promoCommandService, never()).updatePromo(any(), any());
    }


    @Test
    void deletePromo() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/admin/promo/{id}", id))
            .andExpect(status().isAccepted())
            .andExpect(content().string(""));

        verify(promoCommandService).deletePromo(id);
    }
}
