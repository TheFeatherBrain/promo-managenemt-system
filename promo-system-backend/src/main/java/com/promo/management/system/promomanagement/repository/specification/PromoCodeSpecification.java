package com.promo.management.system.promomanagement.repository.specification;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;

import com.promo.management.system.promomanagement.model.entity.PromoCode;
import com.promo.management.system.promomanagement.model.entity.PromoCode_;
import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import org.springframework.data.jpa.domain.Specification;

public class PromoCodeSpecification {

    public static Specification<PromoCode> hasCode(String code) {
        if (isNull(code)) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PromoCode_.CODE), code);
    }

    public static Specification<PromoCode> hasStartDate(LocalDateTime start) {
        if (isNull(start)) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(PromoCode_.EXPIRY_DATE), start);
    }

    public static Specification<PromoCode> hasEndDate(LocalDateTime end) {
        if (isNull(end)) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(PromoCode_.EXPIRY_DATE), end);
    }

    public static Specification<PromoCode> hasStatus(PromoCodeStatus status) {
        if (isNull(status)) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PromoCode_.STATUS), status);
    }

}
