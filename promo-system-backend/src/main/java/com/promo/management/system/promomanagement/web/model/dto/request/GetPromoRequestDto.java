package com.promo.management.system.promomanagement.web.model.dto.request;

import java.time.LocalDateTime;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import com.promo.management.system.promomanagement.model.enumeration.SortBy;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class GetPromoRequestDto {

    private String code;
    private LocalDateTime start;
    private LocalDateTime end;
    private PromoCodeStatus status;
    private SortBy sort = SortBy.CODE;
    private Sort.Direction direction = Sort.Direction.ASC;
    private int pageNumber;
    private int pageSize = 10;

}
