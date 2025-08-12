package com.promo.management.system.promomanagement.web.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatePromoRequestDto {

    @NotEmpty
    private String code;

}
