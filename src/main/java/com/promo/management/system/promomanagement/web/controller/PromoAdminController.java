package com.promo.management.system.promomanagement.web.controller;

import java.util.UUID;

import com.promo.management.system.promomanagement.model.dto.CreatePromoRequestDto;
import com.promo.management.system.promomanagement.model.dto.UpdatePromoRequestDto;
import com.promo.management.system.promomanagement.service.PromoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/promo")
@RequiredArgsConstructor
public class PromoAdminController {

    private final PromoService promoService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createPromo(@RequestBody @Valid CreatePromoRequestDto requestDto) {
        promoService.createPromo(requestDto);
    }

    @PutMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updatePromo(@PathVariable UUID id, @RequestBody @Valid UpdatePromoRequestDto requestDto) {
        promoService.updatePromo(requestDto, id);
    }

}
