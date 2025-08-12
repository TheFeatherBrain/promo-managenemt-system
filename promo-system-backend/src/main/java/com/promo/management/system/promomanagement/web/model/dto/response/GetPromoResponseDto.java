package com.promo.management.system.promomanagement.web.model.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetPromoResponseDto {

    private List<PromoDto> promos;
    private int pageNumber;
    private int pageElements;
    private int totalPages;
    private long totalElements;

}
