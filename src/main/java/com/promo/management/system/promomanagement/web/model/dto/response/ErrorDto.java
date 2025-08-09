package com.promo.management.system.promomanagement.web.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDto {

    private String code;
    private String message;

}
