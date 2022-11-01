package com.greedobank.cards.dto;

import com.greedobank.cards.utils.CardType;
import com.greedobank.cards.validator.ValueOfEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CardTemplateCreationUpdateDTO(

        @NotBlank(message = "Name cannot be null")
        @Size(min = 3, max = 255, message = "Name must have at least 3 characters")
        @ValueOfEnum(enumClass = CardType.class, message = "Type should be one of the types: GOLD, PREMIUM, PLATINUM")
        String type,

        @Valid
        TariffUpdateDTO tariff
) {
}
