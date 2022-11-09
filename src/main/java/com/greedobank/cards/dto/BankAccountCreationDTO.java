package com.greedobank.cards.dto;

import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.validator.ValueOfEnum;

import javax.validation.constraints.NotNull;

public record BankAccountCreationDTO(
        @NotNull(message = "Currency cannot be null")
        @ValueOfEnum(enumClass = CurrencyType.class,
                message = "Currency must be one of the currencies: EUR, CAD, USD, PLN, UAH")
        String currency,

        @NotNull(message = "Card id cannot be null")
        Integer cardId,

        @NotNull(message = "Customer id cannot be null")
        Integer customerId
) {
}
