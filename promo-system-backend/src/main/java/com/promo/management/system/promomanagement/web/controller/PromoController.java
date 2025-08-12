package com.promo.management.system.promomanagement.web.controller;

import com.promo.management.system.promomanagement.service.promo.PromoQueryService;
import com.promo.management.system.promomanagement.web.model.dto.request.GetPromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.response.GetPromoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promo")
@RequiredArgsConstructor
public class PromoController {

    private final PromoQueryService promoQueryService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public GetPromoResponseDto getPromo(GetPromoRequestDto requestDto) {
        return promoQueryService.getPromo(requestDto);
    }

}
