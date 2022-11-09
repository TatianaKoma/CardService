package com.greedobank.cards.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record BankAccountGetBalanceDTO(
        @NotNull(message = "Balance cannot be null")
        @PositiveOrZero(message = "Balance cannot be negative")
        BigDecimal balance
) {
}
