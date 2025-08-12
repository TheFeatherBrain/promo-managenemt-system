package com.promo.management.system.promomanagement.test.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promo.management.system.promomanagement.service.order.OrderService;
import com.promo.management.system.promomanagement.web.controller.OrderController;
import com.promo.management.system.promomanagement.web.model.dto.request.CreateOrderRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.ValidatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.ValidatePromoResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    OrderService orderService;

    @Test
    void validatePromo() throws Exception {
        var req = new ValidatePromoRequestDto();
        req.setCode("SPRING10");

        var serviceRes = ValidatePromoResponseDto.builder()
            .valid(true)
            .message("Promo code is valid.")
            .build();

        when(orderService.validatePromo(any(ValidatePromoRequestDto.class))).thenReturn(serviceRes);

        mvc.perform(post("/order/promo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.valid").value(true))
            .andExpect(jsonPath("$.message").value("Promo code is valid."));

        ArgumentCaptor<ValidatePromoRequestDto> captor = ArgumentCaptor.forClass(ValidatePromoRequestDto.class);
        verify(orderService).validatePromo(captor.capture());
        assertThat(captor.getValue().getCode()).isEqualTo("SPRING10");
    }

    @Test
    void validatePromo_missingCode() throws Exception {
        mvc.perform(post("/order/promo")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder() throws Exception {
        var req = new CreateOrderRequestDto();
        req.setCode("X123");

        doNothing().when(orderService).createOrder(any(CreateOrderRequestDto.class));

        mvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(content().string(""));

        ArgumentCaptor<CreateOrderRequestDto> captor = ArgumentCaptor.forClass(CreateOrderRequestDto.class);
        verify(orderService).createOrder(captor.capture());
        assertThat(captor.getValue().getCode()).isEqualTo("X123");
    }

}
