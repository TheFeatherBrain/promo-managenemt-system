package com.promo.management.system.promomanagement.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDto {

    private String code;
    private String message;

}
