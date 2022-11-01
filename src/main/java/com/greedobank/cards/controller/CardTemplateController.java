package com.greedobank.cards.controller;

import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateCreationUpdateDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.service.CardTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Validated
@RequestMapping(path = "/api/v1/card-template")
@Tag(name = "Card-template", description = "Interaction with card-template")
public class CardTemplateController {
    private final CardTemplateService cardTemplateService;

    @Autowired
    public CardTemplateController(CardTemplateService cardTemplateService) {
        this.cardTemplateService = cardTemplateService;
    }

    @PostMapping
    @Operation(summary = "Create a card-template", description = "Lets create a card-template")
    @PreAuthorize("hasRole('ADMIN')")
    public CardTemplateDTO create(@Valid @RequestBody CardTemplateCreationDTO cardTemplateCreationDTO) {
        return cardTemplateService.create(cardTemplateCreationDTO);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get the card-template by id", description = "Lets get the card-template by id")
    @PreAuthorize("isAuthenticated()")
    public CardTemplateDTO getById(@PathVariable("id") int id) {
        return cardTemplateService.getById(id);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update the card-template ", description = "Lets update the card-template")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(@PathVariable("id") int id,
                       @Valid @RequestBody CardTemplateCreationUpdateDTO forUpdateDTO) {
        cardTemplateService.updateById(id, forUpdateDTO);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the card-template by id", description = "Lets delete the card-template by id")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") int id) {
        cardTemplateService.deleteById(id);
    }
}
