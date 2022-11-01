package com.greedobank.cards.service;

import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateCreationUpdateDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.mapper.CardTemplateMapper;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.utils.CardType;
import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.utils.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class CardTemplateService {
    private static final int USER_ID = 1;

    private final CardTemplateDAO cardTemplateDAO;
    private final CardTemplateMapper mapper;

    @Autowired
    public CardTemplateService(CardTemplateDAO cardTemplateDAO, CardTemplateMapper mapper) {
        this.cardTemplateDAO = cardTemplateDAO;
        this.mapper = mapper;
    }

    public CardTemplateDTO create(CardTemplateCreationDTO cardTemplateCreationDTO) {
        CardTemplate cardTemplate = mapper.toCardTemplate(cardTemplateCreationDTO);
        cardTemplate.setCreatedAt(OffsetDateTime.now());
        cardTemplate.setUpdatedAt(OffsetDateTime.now());
        cardTemplate.setCreatedById(USER_ID);
        cardTemplateDAO.save(cardTemplate);
        return mapper.toCardTemplateDTO(cardTemplate);
    }

    public void updateById(int id, CardTemplateCreationUpdateDTO forUpdateDTO) {
        CardTemplate cardTemplate = cardTemplateDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CARD_TEMPLATE_NOT_FOUND, id)));

        if (forUpdateDTO.type() != null) {
            cardTemplate.setType(CardType.valueOf(forUpdateDTO.type()));
        }
        if (forUpdateDTO.tariff().issueCost() != null) {
            cardTemplate.setIssueCost(forUpdateDTO.tariff().issueCost());
        }
        if (forUpdateDTO.tariff().serviceCost() != null) {
            cardTemplate.setServiceCost(forUpdateDTO.tariff().serviceCost());
        }
        if (forUpdateDTO.tariff().reissueCost() != null) {
            cardTemplate.setReissueCost(forUpdateDTO.tariff().reissueCost());
        }
        if (forUpdateDTO.tariff().currency() != null) {
            cardTemplate.setCurrency(CurrencyType.valueOf(forUpdateDTO.tariff().currency()));
        }
        cardTemplateDAO.save(cardTemplate);
    }

    public void deleteById(int id) {
        CardTemplate cardTemplate = cardTemplateDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CARD_TEMPLATE_NOT_FOUND, id)));
        cardTemplateDAO.delete(cardTemplate);
    }

    public CardTemplateDTO getById(int id) {
        return cardTemplateDAO.findById(id)
                .map(mapper::toCardTemplateDTO)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CARD_TEMPLATE_NOT_FOUND, id)));
    }
}
