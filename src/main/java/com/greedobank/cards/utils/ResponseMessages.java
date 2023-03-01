package com.greedobank.cards.utils;

public enum ResponseMessages {
    CARD_NOT_FOUND("Card with id %s was not found"),
    CUSTOMER_NOT_FOUND("Customer with id %s was not found"),
    CARD_TEMPLATE_NOT_FOUND("Card-template with id %s was not found"),
    VALIDATION_ERROR("Validation error"),
    RESOURCE_NOT_FOUND("The requested resource is not found"),
    NO_ACCESS("You do not have access to this option"),
    AUTH_HEADER_NAME("Authorization"),
    AUTH_HEADER_PREFIX("Bearer "),
    TOKEN_WRONG("Token is wrong"),
    TOKEN_EXPIRED("Token is expired"),
    TEMPLATE_MESSAGE("{\"reason\": \"%s\"}"),
    BANK_ACCOUNT_NOT_FOUND("Bank account with id %s was not found"),
    BANK_BIN("473117"),
    CARD_NUMBER_DOES_NOT_MATCH_BANK_ACCOUNT("Card number %s does not match bank account"),
    CARD_NUMBER_IS_NOT_VALID("Card number is not valid"),
    CARD_IS_NOT_ACTIVE("Card is not active"),
    VALID_NUMBER("Is valid number: "),
    VALID_PIN("Is valid pin: "),
    VALID_CVV("Is valid cvv: "),
    VALID_END_DATE("Is valid end date: "),
    NOT_ENOUGH_FUNDS("Not enough funds in the account"),
    PIN_NOT_CORRECT("Pin code %s does not correct"),
    CVV_NOT_CORRECT("Cvv code %s does not correct"),
    END_DATE_NOT_CORRECT("Expired date %s does not correct");

    private final String description;

    ResponseMessages(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
