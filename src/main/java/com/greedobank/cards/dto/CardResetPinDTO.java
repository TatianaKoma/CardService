package com.greedobank.cards.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CardResetPinDTO(

        @NotBlank(message = "Pin code cannot be null")
        @Size(max = 4, message = "Pin cannot be more than 4 digits")
        String pin
){
}
