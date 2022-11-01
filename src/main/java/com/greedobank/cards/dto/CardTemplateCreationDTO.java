package com.greedobank.cards.dto;

import com.greedobank.cards.utils.CardType;
import com.greedobank.cards.validator.ValueOfEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CardTemplateCreationDTO(

        @NotBlank(message = "Type cannot be null")
        @ValueOfEnum(enumClass = CardType.class, message = "Type should be one of the types: GOLD, PREMIUM, PLATINUM")
        String type,

        @Valid
        @NotNull(message = "Tariff cannot be null")
        TariffDTO tariff
) {
}
