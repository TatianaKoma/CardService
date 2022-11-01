package com.greedobank.cards.utils;

public class ResponseMessages {
    public static final String CARD_NOT_FOUND = "Card with id %s was not found";
    public static final String CUSTOMER_NOT_FOUND = "Customer with id %s was not found";
    public static final String CARD_TEMPLATE_NOT_FOUND = "Card-template with id %s was not found";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String RESOURCE_NOT_FOUND = "The requested resource is not found";
    public static final String NO_ACCESS = "You do not have access to this option";
    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_PREFIX = "Bearer ";
    public static final String TOKEN_WRONG = "Token is wrong";
    public static final String TOKEN_EXPIRED = "Token is expired";
    public static final String TEMPLATE_MESSAGE = "{\"reason\": \"%s\"}";
    public static final String BANK_ACCOUNT_NOT_FOUND = "Bank account with id %s was not found";
    public static final String BANK_BIN = "473117";
    public static final String CARD_NUMBER_DOES_NOT_MATCH_BANK_ACCOUNT = "Card number %s does not match bank account";
    public static final String CARD_NUMBER_IS_NOT_VALID = "Card number is not valid";
    public static final String CARD_IS_NOT_ACTIVE = "Card is not active";
    public static final String VALID_NUMBER = "Is valid number: ";
    public static final String VALID_PIN = "Is valid pin: ";
    public static final String VALID_CVV = "Is valid cvv: ";
    public static final String VALID_END_DATE = "Is valid end date: ";
    public static final String NOT_ENOUGH_FUNDS = "Not enough funds in the account";
    public static final String PIN_NOT_CORRECT = "Pin code %s does not correct";
    public static final String CVV_NOT_CORRECT = "Cvv code %s does not correct";
    public static final String END_DATE_NOT_CORRECT = "Expired date %s does not correct";
}
