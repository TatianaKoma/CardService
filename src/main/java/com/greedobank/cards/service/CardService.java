package com.greedobank.cards.service;

import com.greedobank.cards.dao.CardDAO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.greedobank.cards.utils.ResponseMessages.CARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardDAO cardDAO;

    public Card create(Card card) {
        cardDAO.save(card);
        return card;
    }

    public Card getById(int id) {
        return cardDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CARD_NOT_FOUND.getDescription(), id)));
    }

    public void updatePin(int id, Card cardResetPin) {
        Card card = cardDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CARD_NOT_FOUND.getDescription(), id)));
        card.setPin(cardResetPin.getPin());
        cardDAO.save(card);
    }
}
