package com.greedobank.cards.dto;

import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.validator.ValueOfEnum;

import javax.validation.constraints.NotNull;

public record CardCreationDTO(
        String firstName,
        String lastName,

        @NotNull(message = "Currency cannot be null")
        @ValueOfEnum(enumClass = CurrencyType.class,
                message = "Currency must be one of the currencies: EUR, CAD, USD, PLN, UAH")
        String currency,

        @NotNull(message = "Card template id cannot be null")
        Integer cardTemplateId,

        @NotNull(message = "Customer id cannot be null")
        Integer customerId
) {
}
