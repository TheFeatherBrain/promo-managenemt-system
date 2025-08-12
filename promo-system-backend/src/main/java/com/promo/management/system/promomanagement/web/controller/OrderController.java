package com.promo.management.system.promomanagement.web.controller;

import com.promo.management.system.promomanagement.service.order.OrderService;
import com.promo.management.system.promomanagement.web.model.dto.request.CreateOrderRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.ValidatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.ValidatePromoResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/promo")
    @ResponseStatus(HttpStatus.OK)
    public ValidatePromoResponseDto validatePromo(@RequestBody @Valid ValidatePromoRequestDto requestDto) {
        return orderService.validatePromo(requestDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody @Valid CreateOrderRequestDto requestDto) {
        orderService.createOrder(requestDto);
    }

}
