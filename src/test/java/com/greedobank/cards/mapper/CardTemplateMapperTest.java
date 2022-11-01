package com.greedobank.cards.mapper;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.utils.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTemplateMapperTest {
    private CardTemplateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CardTemplateMapper();
    }

    @Test
    void shouldReturnCardTemplateWhenToCardTemplate() {
        CardTemplateCreationDTO cardTemplateCreationDTO =
                EntityInitializer.getCardTemplateCreationDTO(1);

        CardTemplate cardTemplate = mapper.toCardTemplate(cardTemplateCreationDTO);

        assertEquals(cardTemplateCreationDTO.type(), cardTemplate.getType().toString());
        assertEquals(cardTemplateCreationDTO.tariff().issueCost(), cardTemplate.getIssueCost());
        assertEquals(cardTemplateCreationDTO.tariff().serviceCost(), cardTemplate.getServiceCost());
        assertEquals(cardTemplateCreationDTO.tariff().reissueCost(), cardTemplate.getReissueCost());
        assertEquals(CurrencyType.valueOf(cardTemplateCreationDTO.tariff().currency()), cardTemplate.getCurrency());
    }

    @Test
    void shouldReturnCardTemplateDTOWhenToCardTemplateDTO() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        CardTemplateDTO cardTemplateDTO = mapper.toCardTemplateDTO(cardTemplate);

        assertEquals(cardTemplate.getId(), cardTemplateDTO.id());
        assertEquals(cardTemplate.getType().toString(), cardTemplateDTO.type());
        assertEquals(cardTemplate.getIssueCost(), cardTemplateDTO.tariff().issueCost());
        assertEquals(cardTemplate.getServiceCost(), cardTemplateDTO.tariff().serviceCost());
        assertEquals(cardTemplate.getReissueCost(), cardTemplateDTO.tariff().reissueCost());
        assertEquals(cardTemplate.getCurrency().toString(), cardTemplateDTO.tariff().currency());
        assertEquals(cardTemplate.getCreatedAt(), cardTemplateDTO.createdAt());
        assertEquals(cardTemplate.getUpdatedAt(), cardTemplateDTO.updatedAt());
        assertEquals(cardTemplate.getCreatedById(), cardTemplateDTO.createdById());
    }
}
