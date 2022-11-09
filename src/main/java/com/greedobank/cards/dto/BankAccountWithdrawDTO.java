package com.greedobank.cards.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public record BankAccountWithdrawDTO(
        @NotNull(message = "Balance cannot be null")
        @PositiveOrZero(message = "Balance cannot be negative")
        BigDecimal balance,

        @NotBlank(message = "Card number cannot be null")
        @Size(max = 16, message = "Card number cannot be more than 16 digits")
        String cardNumber,

        @NotBlank(message = "Pin code cannot be null")
        @Size(max = 4, message = "Pin cannot be more than 4 digits")
        String pin
) {
}
