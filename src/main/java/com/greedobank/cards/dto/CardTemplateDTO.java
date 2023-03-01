package com.greedobank.cards.dto;

import java.time.OffsetDateTime;

public record CardTemplateDTO(
        int id,
        String type,
        TariffDTO tariff,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        int createdById
) {
}
