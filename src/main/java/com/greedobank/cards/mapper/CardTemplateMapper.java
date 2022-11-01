package com.greedobank.cards.mapper;

import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.dto.TariffDTO;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.utils.CardType;
import com.greedobank.cards.utils.CurrencyType;
import org.springframework.stereotype.Component;

@Component
public class CardTemplateMapper {
    public CardTemplate toCardTemplate(CardTemplateCreationDTO cardTemplateCreationDTO) {
        CardTemplate cardTemplate = new CardTemplate();
        cardTemplate.setType(CardType.valueOf(cardTemplateCreationDTO.type()));
        cardTemplate.setIssueCost(cardTemplateCreationDTO.tariff().issueCost());
        cardTemplate.setServiceCost(cardTemplateCreationDTO.tariff().serviceCost());
        cardTemplate.setReissueCost(cardTemplateCreationDTO.tariff().reissueCost());
        cardTemplate.setCurrency(CurrencyType.valueOf(cardTemplateCreationDTO.tariff().currency()));
        return cardTemplate;
    }

    public CardTemplateDTO toCardTemplateDTO(CardTemplate cardTemplate) {
        TariffDTO tariffDTO = new TariffDTO(cardTemplate.getIssueCost(),
                cardTemplate.getServiceCost(),
                cardTemplate.getReissueCost(),
                cardTemplate.getCurrency().toString());
        return new CardTemplateDTO(cardTemplate.getId(),
                cardTemplate.getType().toString(),
                tariffDTO,
                cardTemplate.getCreatedAt(),
                cardTemplate.getUpdatedAt(),
                cardTemplate.getCreatedById());
    }
}
