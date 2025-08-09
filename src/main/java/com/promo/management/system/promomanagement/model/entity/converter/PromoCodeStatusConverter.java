package com.promo.management.system.promomanagement.model.entity.converter;

import java.util.Objects;

import com.promo.management.system.promomanagement.model.enumeration.PromoCodeStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PromoCodeStatusConverter implements AttributeConverter<PromoCodeStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PromoCodeStatus promoCodeStatus) {
        return Objects.isNull(promoCodeStatus) ? null : promoCodeStatus.getCode();
    }

    @Override
    public PromoCodeStatus convertToEntityAttribute(Integer dbData) {
        return PromoCodeStatus.ofCode(dbData);
    }
}
