package com.promo.management.system.promomanagement.web.model.dto.request;

import java.time.LocalDateTime;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPromoRequestDto {

    private String code;
    private LocalDateTime start;
    private LocalDateTime end;
    private PromoCodeStatus status;
    private int pageNumber;
    private int pageSize = 10;

}
