package com.promo.management.system.promomanagement.web.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidatePromoResponseDto {

    private boolean valid;
    private String message;

}
