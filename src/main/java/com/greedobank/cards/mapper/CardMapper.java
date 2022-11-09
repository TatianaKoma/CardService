package com.greedobank.cards.mapper;

import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.dao.CustomerDAO;
import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.dto.CardResetPinDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.model.Customer;
import com.greedobank.cards.utils.CurrencyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static com.greedobank.cards.utils.ResponseMessages.CARD_TEMPLATE_NOT_FOUND;
import static com.greedobank.cards.utils.ResponseMessages.CUSTOMER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CardMapper {
    private final CardTemplateDAO cardTemplateDAO;
    private final CustomerDAO customerDAO;

    public Card toCard(CardCreationDTO cardCreationDTO) {
        Card card = new Card();
        card.setFirstName(cardCreationDTO.firstName());
        card.setLastName(cardCreationDTO.lastName());
        card.setCurrency(CurrencyType.valueOf(cardCreationDTO.currency()));
        CardTemplate cardTemplate = cardTemplateDAO.findById(cardCreationDTO.cardTemplateId())
                .orElseThrow(() -> new NotFoundException(String.format(CARD_TEMPLATE_NOT_FOUND.getDescription(),
                        cardCreationDTO.cardTemplateId())));
        card.setCardTemplate(cardTemplate);
        Customer customer = customerDAO.findById(cardCreationDTO.customerId())
                .orElseThrow(() -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND.getDescription(),
                        cardCreationDTO.customerId())));
        card.setCustomer(customer);
        return card;
    }

    public Card toCard(CardResetPinDTO cardResetPinDTO) {
        Card card = new Card();
        card.setPin(cardResetPinDTO.pin());
        return card;
    }

    public CardDTO toCardDTO(Card card) {
        return new CardDTO(card.getId(),
                card.getFirstName(),
                card.getLastName(),
                card.getNumber(),
                card.getPin(),
                card.getCvv(),
                DateTimeFormatter.ofPattern("MM/yy").format(card.getEndDate()),
                card.isActive(),
                card.getCurrency().toString());
    }
}
