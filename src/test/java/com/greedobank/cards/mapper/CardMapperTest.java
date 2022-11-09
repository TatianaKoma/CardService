package com.greedobank.cards.mapper;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.dao.CustomerDAO;
import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CardMapperTest {
    @InjectMocks
    private CardMapper mapper;
    @Mock
    private CardTemplateDAO cardTemplateDAO;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCardWhenToCard() {
        Customer customer = EntityInitializer.getCustomer(1);
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        Card card = EntityInitializer.getCard(1);
        CardCreationDTO cardCreationDTO = EntityInitializer.getCardCreationDTO(1);
        when(cardTemplateDAO.findById(cardCreationDTO.cardTemplateId())).thenReturn(Optional.of(cardTemplate));
        when(customerDAO.findById(cardCreationDTO.customerId())).thenReturn(Optional.of(customer));

        assertEquals(cardCreationDTO.firstName(), card.getFirstName());
        assertEquals(cardCreationDTO.lastName(), card.getLastName());
        assertEquals(cardCreationDTO.currency(), card.getCurrency().toString());
    }

    @Test
    void shouldReturnCardDTOWhenToCardDTO() {
        Card card = EntityInitializer.getCard(1);
        CardDTO cardDTO = mapper.toCardDTO(card);

        assertEquals(card.getId(), cardDTO.id());
        assertEquals(card.getFirstName(), cardDTO.firstName());
        assertEquals(card.getLastName(), cardDTO.lastName());
        assertEquals(card.getNumber(), cardDTO.number());
        assertEquals(card.getPin(), cardDTO.pin());
        assertEquals(card.getCvv(), cardDTO.cvv());
        assertEquals(DateTimeFormatter.ofPattern("MM/yy").format(card.getEndDate()), cardDTO.endDate());
        assertEquals(card.isActive(), cardDTO.active());
        assertEquals(card.getCurrency().toString(), cardDTO.currency());
    }
}
