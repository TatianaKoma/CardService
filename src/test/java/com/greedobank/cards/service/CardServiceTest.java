package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.CardDAO;
import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.dto.CardResetPinDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.mapper.CardMapper;
import com.greedobank.cards.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceTest {
    private static final int CARD_ID = 1;

    @InjectMocks
    private com.greedobank.cards.service.CardService service;
    @Mock
    private CardDAO cardDAO;
    @Mock
    private CardMapper mapper;
    @Spy
    private ValidationCardService validationCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCardTemplateDTOWhenCreate() {
        CardCreationDTO cardCreationDTO = EntityInitializer.getCardCreationDTO(1);
        CardDTO cardDTO = EntityInitializer.getCardDTO(1);
        Card card = EntityInitializer.getCard(1);
        when(cardDAO.save(card)).thenReturn(card);
        when(mapper.toCard(cardCreationDTO)).thenReturn(card);
        when(mapper.toCardDTO(card)).thenReturn(cardDTO);
        CardDTO createdCardDTO = service.create(cardCreationDTO);

        assertNotNull(createdCardDTO);
        assertEquals(cardDTO.id(), createdCardDTO.id());
        assertEquals(cardDTO.firstName(), createdCardDTO.firstName());
        assertEquals(cardDTO.lastName(), createdCardDTO.lastName());
        assertEquals(cardDTO.number(), createdCardDTO.number());
        assertEquals(cardDTO.pin(), createdCardDTO.pin());
        assertEquals(cardDTO.cvv(), createdCardDTO.cvv());
        assertEquals(cardDTO.endDate(), createdCardDTO.endDate());
        assertEquals(cardDTO.active(), createdCardDTO.active());
        assertEquals(cardDTO.currency(), createdCardDTO.currency());
    }

    @Test
    void shouldReturnCardDTOWhenGetById() {
        CardDTO cardDTO = EntityInitializer.getCardDTO(1);
        Card card = EntityInitializer.getCard(1);

        when(mapper.toCardDTO(card)).thenReturn(cardDTO);
        when(cardDAO.findById(anyInt())).thenReturn(Optional.of(card));
        CardDTO actualCardDTO = service.getById(cardDTO.id());

        assertNotNull(actualCardDTO);
        assertThat(actualCardDTO).isSameAs(cardDTO);
        verify(cardDAO).findById(cardDTO.id());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetByIdNotFound() {
        when(cardDAO.findById(CARD_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getById(CARD_ID));

        assertNotNull(thrown);
        verify(cardDAO).findById(CARD_ID);
    }

    @Test
    void shouldUpdateByIdWhenUpdatePin() {
        CardResetPinDTO cardResetPinDTO = EntityInitializer.getCardResetPinDTO(1);
        Card card = EntityInitializer.getCard(1);

        when(cardDAO.findById(anyInt())).thenReturn(Optional.of(card));
        when(cardDAO.save(card)).thenReturn(card);
        service.updatePin(card.getId(), cardResetPinDTO);

        assertEquals(cardResetPinDTO.pin(), card.getPin());
        verify(cardDAO).findById(CARD_ID);
        verify(cardDAO).save(card);
    }

    @Test
    void shouldReturn404WhenUpdatePinNotFound() {
        CardResetPinDTO cardResetPinDTO = EntityInitializer.getCardResetPinDTO(1);

        when(cardDAO.findById(CARD_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.updatePin(CARD_ID, cardResetPinDTO));
        assertNotNull(thrown);
        verify(cardDAO).findById(CARD_ID);
    }
}
