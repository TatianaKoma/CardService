package com.greedobank.cards.dto;

import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.validator.ValueOfEnum;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public record TariffDTO(
        @NotNull(message = "IssueCost cannot be null")
        @PositiveOrZero(message = "IssueCost cannot be negative")
        @Digits(integer = 6, fraction = 2, message = "IssueCost cannot contain more than two digits of precision " +
                "and more than 6 digits before it")
        Double issueCost,

        @NotNull(message = "ServiceCost cannot be null")
        @PositiveOrZero(message = "ServiceCost cannot be negative")
        @Digits(integer = 6, fraction = 2, message = "ServiceCost cannot contain more than two digits of precision " +
                "and more than 6 digits before it")
        Double serviceCost,

        @NotNull(message = "ReissueCost cannot be null")
        @PositiveOrZero(message = "ReissueCost cannot be negative")
        @Digits(integer = 6, fraction = 2, message = "ReissueCost cannot contain more than two digits of precision " +
                "and more than 6 digits before it")
        Double reissueCost,

        @NotNull(message = "Currency cannot be null")
        @ValueOfEnum(enumClass = CurrencyType.class, message = "Currency must be one of the currencies: EUR, CAD, USD, PLN, UAH")
        String currency
) {
}
