package com.greedobank.cards.dto;

public record CardDTO(
        int id,
        String firstName,
        String lastName,
        String number,
        String pin,
        String cvv,
        String endDate,
        boolean active,
        String currency
) {
}
