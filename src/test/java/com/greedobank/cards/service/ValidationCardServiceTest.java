package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.model.Customer;
import com.greedobank.cards.utils.CurrencyType;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationCardServiceTest {
    private final ValidationCardService validationCardService = new ValidationCardService();

    @Test
    void shouldReturnTrueWhenCheckValidCard() {
        Card card = EntityInitializer.getCard(2);

        boolean status = validationCardService
                .check(card)
                .number()
                .pin()
                .cvv()
                .endDate()
                .validate();

        assertTrue(status);
    }

    @Test
    void shouldReturnFalseWhenCheckCardWithInvalidNumber() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        Customer customer = EntityInitializer.getCustomer(1);
        Card card = new Card(1, "Tom", "Ford", "4731179262995090", "6536", "368",
                OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, cardTemplate,
                customer, new BankAccount());

        boolean status = validationCardService
                .check(card)
                .number()
                .pin()
                .cvv()
                .endDate()
                .validate();

        assertFalse(status);
    }

    @Test
    void shouldReturnFalseWhenCheckCardWithInvalidPin() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        Customer customer = EntityInitializer.getCustomer(1);
        Card card = new Card(1, "Tom", "Ford", "4731179262995095", "653", "368",
                OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, cardTemplate,
                customer, new BankAccount());

        boolean status = validationCardService
                .check(card)
                .number()
                .pin()
                .cvv()
                .endDate()
                .validate();

        assertFalse(status);
    }

    @Test
    void shouldReturnFalseWhenCheckCardWithInvalidCvv() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        Customer customer = EntityInitializer.getCustomer(1);
        Card card = new Card(1, "Tom", "Ford", "4731179262995095", "6536", "3685",
                OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, cardTemplate,
                customer, new BankAccount());

        boolean status = validationCardService
                .check(card)
                .number()
                .pin()
                .cvv()
                .endDate()
                .validate();

        assertFalse(status);
    }

    @Test
    void shouldReturnFalseWhenCheckCardWithInvalidEndDate() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        Customer customer = EntityInitializer.getCustomer(1);
        Card card = new Card(1, "Tom", "Ford", "4731179262995095", "6536", "368",
                OffsetDateTime.parse("2021-09-30T10:50:30+01:00"), true, CurrencyType.UAH, cardTemplate,
                customer, new BankAccount());

        boolean status = validationCardService
                .check(card)
                .number()
                .pin()
                .cvv()
                .endDate()
                .validate();

        assertFalse(status);
    }

    @Test
    void shouldReturnTrueWhenCheckValidCardWithSomeDetails() {
        Card card = EntityInitializer.getCard(1);

        boolean status = validationCardService
                .check(card)
                .number()
                .pin()
                .endDate()
                .validate();

        assertTrue(status);
    }

    @Test
    void shouldReturnFalseWhenCheckInvalidCardWithSomeDetails() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        Customer customer = EntityInitializer.getCustomer(1);
        Card card = new Card(1, "Tom", "Ford", "4731179262995095", "6536", "36",
                OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, cardTemplate,
                customer, new BankAccount());

        boolean status = validationCardService
                .check(card)
                .number()
                .cvv()
                .endDate()
                .validate();

        assertFalse(status);
    }

    @Test
    void shouldReturnTrueWhenCheckValidCardNumber() {
        Card card = EntityInitializer.getCard(2);

        boolean status = validationCardService
                .check(card)
                .number()
                .validate();
        assertTrue(status);
    }
}
