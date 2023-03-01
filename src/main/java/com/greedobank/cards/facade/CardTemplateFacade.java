package com.greedobank.cards.facade;

import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateCreationUpdateDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.mapper.CardTemplateMapper;
import com.greedobank.cards.model.CardTemplate;
import com.greedobank.cards.service.CardTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class CardTemplateFacade {
    private final CardTemplateService cardTemplateService;
    private final CardTemplateMapper cardTemplateMapper;

    public CardTemplateDTO create(CardTemplateCreationDTO cardTemplateCreationDTO) {
        CardTemplate cardTemplate = cardTemplateMapper.toCardTemplate(cardTemplateCreationDTO);
        cardTemplate.setCreatedAt(OffsetDateTime.now());
        cardTemplate.setUpdatedAt(OffsetDateTime.now());
        cardTemplate.setCreatedById(cardTemplateCreationDTO.createdById());
        CardTemplate cartTemplateCreated = cardTemplateService.create(cardTemplate);
        return cardTemplateMapper.toCardTemplateDTO(cartTemplateCreated);
    }

    public void updateById(int id, CardTemplateCreationUpdateDTO forUpdateDTO) {
        CardTemplate cardTemplate = cardTemplateMapper.toCardTemplate(forUpdateDTO);
        cardTemplateService.updateById(id, cardTemplate);
    }

    public void deleteById(int id) {
        cardTemplateService.deleteById(id);
    }

    public CardTemplateDTO getById(int id) {
        CardTemplate cardTemplate = cardTemplateService.getById(id);
        return cardTemplateMapper.toCardTemplateDTO(cardTemplate);
    }
}
