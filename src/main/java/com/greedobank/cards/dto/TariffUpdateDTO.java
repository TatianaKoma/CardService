package com.greedobank.cards.dto;

import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.validator.ValueOfEnum;

import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;

public record TariffUpdateDTO(
        @PositiveOrZero(message = "IssueCost cannot be negative")
        @Digits(integer = 6, fraction = 2, message = "IssueCost cannot contain more than two digits of precision " +
                "and more than 6 digits before it")
        Double issueCost,

        @PositiveOrZero(message = "ServiceCost cannot be negative")
        @Digits(integer = 6, fraction = 2, message = "ServiceCost cannot contain more than two digits of precision " +
                "and more than 6 digits before it")
        Double serviceCost,

        @PositiveOrZero(message = "ReissueCost cannot be negative")
        @Digits(integer = 6, fraction = 2, message = "ReissueCost cannot contain more than two digits of precision " +
                "and more than 6 digits before it")
        Double reissueCost,

        @ValueOfEnum(enumClass = CurrencyType.class, message = "Must be one of currencies: EUR, CAD, USD, PLN, UAH")
        String currency
) {
}
