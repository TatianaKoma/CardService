package com.greedobank.cards.mapper;

import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.dao.CustomerDAO;
import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.model.Customer;
import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.utils.ResponseMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

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
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CARD_TEMPLATE_NOT_FOUND,
                        cardCreationDTO.cardTemplateId())));
        card.setCardTemplate(cardTemplate);
        Customer customer = customerDAO.findById(cardCreationDTO.customerId())
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CUSTOMER_NOT_FOUND,
                        cardCreationDTO.customerId())));
        card.setCustomer(customer);
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
