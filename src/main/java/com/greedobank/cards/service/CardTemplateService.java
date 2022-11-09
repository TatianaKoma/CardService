package com.greedobank.cards.service;

import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.CardTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.greedobank.cards.utils.ResponseMessages.CARD_TEMPLATE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CardTemplateService {
    private final CardTemplateDAO cardTemplateDAO;

    public CardTemplate create(CardTemplate cardTemplate) {
        cardTemplateDAO.save(cardTemplate);
        return cardTemplate;
    }

    public void updateById(int id, CardTemplate forUpdateDTO) {
        CardTemplate cardTemplate = cardTemplateDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CARD_TEMPLATE_NOT_FOUND.getDescription(), id)));

        if (forUpdateDTO.getType() != null) {
            cardTemplate.setType(forUpdateDTO.getType());
        }
        if (forUpdateDTO.getIssueCost() != null) {
            cardTemplate.setIssueCost(forUpdateDTO.getIssueCost());
        }
        if (forUpdateDTO.getServiceCost() != null) {
            cardTemplate.setServiceCost(forUpdateDTO.getServiceCost());
        }
        if (forUpdateDTO.getReissueCost() != null) {
            cardTemplate.setReissueCost(forUpdateDTO.getReissueCost());
        }
        if (forUpdateDTO.getCurrency() != null) {
            cardTemplate.setCurrency(forUpdateDTO.getCurrency());
        }
        cardTemplateDAO.save(cardTemplate);
    }

    public void deleteById(int id) {
        CardTemplate cardTemplate = cardTemplateDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CARD_TEMPLATE_NOT_FOUND.getDescription(), id)));
        cardTemplateDAO.delete(cardTemplate);
    }

    public CardTemplate getById(int id) {
        return cardTemplateDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CARD_TEMPLATE_NOT_FOUND.getDescription(), id)));
    }
}
