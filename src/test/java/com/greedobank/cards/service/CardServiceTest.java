package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.CardDAO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.utils.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.greedobank.cards.EntityInitializer.getCardTemplate;
import static com.greedobank.cards.EntityInitializer.getCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceTest {
    @InjectMocks
    private CardService service;

    @Mock
    private CardDAO cardDAO;

    @Spy
    private ValidationCardService validationCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCardTemplateDTOWhenCreate() {
        Card card = EntityInitializer.getCard(1);

        when(cardDAO.save(card)).thenReturn(card);
        Card createdCard = service.create(card);

        assertNotNull(createdCard);
        assertEquals(card.getId(), createdCard.getId());
        assertEquals(card.getFirstName(), createdCard.getFirstName());
        assertEquals(card.getLastName(), createdCard.getLastName());
        assertEquals(card.getNumber(), createdCard.getNumber());
        assertEquals(card.getPin(), createdCard.getPin());
        assertEquals(card.getCvv(), createdCard.getCvv());
        assertEquals(card.getEndDate(), createdCard.getEndDate());
        assertEquals(card.isActive(), createdCard.isActive());
        assertEquals(card.getCurrency(), createdCard.getCurrency());
    }

    @Test
    void shouldReturnCardWhenGetById() {
        Card card = EntityInitializer.getCard(1);

        when(cardDAO.findById(anyInt())).thenReturn(Optional.of(card));
        Card actualCard = service.getById(card.getId());

        assertNotNull(actualCard);
        assertThat(actualCard).isSameAs(card);
        verify(cardDAO).findById(card.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetByIdNotFound() {
        Card card = EntityInitializer.getCard(1);
        when(cardDAO.findById(card.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getById(card.getId()));

        assertNotNull(thrown);
        verify(cardDAO).findById(card.getId());
    }

    @Test
    void shouldUpdateByIdWhenUpdatePin() {
        Card card = EntityInitializer.getCard(1);
        Card newCard = new Card();
        newCard.setPin("1234");
        Card expectedCard =  new Card(1, "Tom", "Ford", "4731179262995095", "1234", "368",
                OffsetDateTime.parse("2025-09-30T10:50:30+01:00"), true, CurrencyType.UAH, getCardTemplate(1),
                getCustomer(1), new BankAccount());

        when(cardDAO.findById(anyInt())).thenReturn(Optional.of(card));
        when(cardDAO.save(card)).thenReturn(card);
        service.updatePin(card.getId(),newCard);

        assertEquals(expectedCard.getPin(), card.getPin());
        verify(cardDAO).findById(card.getId());
        verify(cardDAO).save(card);
    }

    @Test
    void shouldReturn404WhenUpdatePinNotFound() {
        Card card = EntityInitializer.getCard(1);
        Card newCard = new Card();
        newCard.setPin("1234");

        when(cardDAO.findById(card.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.updatePin(card.getId(),newCard));
        assertNotNull(thrown);
        verify(cardDAO).findById(card.getId());
    }
}
