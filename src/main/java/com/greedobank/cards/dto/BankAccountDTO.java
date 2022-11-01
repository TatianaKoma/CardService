package com.greedobank.cards.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record BankAccountDTO(
        Integer id,
        String iban,
        BigDecimal balance,
        String currency,
        OffsetDateTime created_at,
        boolean active
) {
}
