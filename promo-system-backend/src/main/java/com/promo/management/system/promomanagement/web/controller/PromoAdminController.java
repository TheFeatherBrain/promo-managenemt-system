package com.promo.management.system.promomanagement.web.controller;

import java.util.UUID;

import com.promo.management.system.promomanagement.web.model.dto.request.CreatePromoRequestDto;
import com.promo.management.system.promomanagement.web.model.dto.request.UpdatePromoRequestDto;
import com.promo.management.system.promomanagement.service.promo.PromoCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private final PromoCommandService promoCommandService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createPromo(@RequestBody @Valid CreatePromoRequestDto requestDto) {
        promoCommandService.createPromo(requestDto);
    }

    @PutMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updatePromo(@PathVariable UUID id, @RequestBody @Valid UpdatePromoRequestDto requestDto) {
        promoCommandService.updatePromo(requestDto, id);
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void deletePromo(@PathVariable UUID id) {
        promoCommandService.deletePromo(id);
    }

}
