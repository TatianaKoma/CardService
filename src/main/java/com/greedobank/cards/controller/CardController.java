package com.greedobank.cards.controller;

import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.dto.CardResetPinDTO;
import com.greedobank.cards.facade.CardFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api/v1/card")
@Tag(name = "Card", description = "Interaction with cards")
public class CardController {
    private final CardFacade cardFacade;

    @PostMapping
    @Operation(summary = "Create a card", description = "Lets create a card")
    @PreAuthorize("hasRole('ADMIN')")
    public CardDTO create(@Valid @RequestBody CardCreationDTO cardCreationDTO) {
        return cardFacade.create(cardCreationDTO);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get the card by id", description = "Lets get the card by id")
    @PreAuthorize("isAuthenticated()")
    public CardDTO getById(@PathVariable("id") int id) {
        return cardFacade.getById(id);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update pin code by card id", description = "Lets update pin code by card id")
    @PreAuthorize("isAuthenticated()")
    public void updatePin(@PathVariable("id") int id, @Valid @RequestBody CardResetPinDTO cardResetPinDTO) {
        cardFacade.updatePin(id, cardResetPinDTO);
    }
}
