package com.promo.management.system.promomanagement.web.controller;

import com.promo.management.system.promomanagement.service.PromoCodeService;
import com.promo.management.system.promomanagement.model.dto.CreatePromoCodeRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promo")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createPromoCode(@RequestBody @Valid CreatePromoCodeRequestDto requestDto) {
        promoCodeService.createPromoCode(requestDto);
    }

}
