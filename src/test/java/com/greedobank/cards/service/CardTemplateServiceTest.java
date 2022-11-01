package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateCreationUpdateDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.mapper.CardTemplateMapper;
import com.greedobank.cards.model.CardTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardTemplateServiceTest {
    private static final int CARD_TEMPLATE_ID = 1;

    @InjectMocks
    private CardTemplateService service;
    @Mock
    private CardTemplateDAO cardTemplateDAO;
    @Mock
    private CardTemplateMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCardTemplateDTOWhenCreate() {
        CardTemplateCreationDTO cardTemplateCreationDTO = EntityInitializer.getCardTemplateCreationDTO(1);
        CardTemplateDTO cardTemplateDTO = EntityInitializer.getCardTemplateDTO(1);
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(cardTemplateDAO.save(cardTemplate)).thenReturn(cardTemplate);
        when(mapper.toCardTemplate(cardTemplateCreationDTO)).thenReturn(cardTemplate);
        when(mapper.toCardTemplateDTO(cardTemplate)).thenReturn(cardTemplateDTO);
        CardTemplateDTO createdCardTemplateDTO = service.create(cardTemplateCreationDTO);

        assertNotNull(createdCardTemplateDTO);
        assertEquals(cardTemplateDTO.id(), createdCardTemplateDTO.id());
        assertEquals(cardTemplateDTO.type(), createdCardTemplateDTO.type());
        assertEquals(cardTemplateDTO.tariff().issueCost(), createdCardTemplateDTO.tariff().issueCost());
        assertEquals(cardTemplateDTO.tariff().serviceCost(), createdCardTemplateDTO.tariff().serviceCost());
        assertEquals(cardTemplateDTO.tariff().reissueCost(), createdCardTemplateDTO.tariff().reissueCost());
        assertEquals(cardTemplateDTO.tariff().currency(), createdCardTemplateDTO.tariff().currency());
        assertEquals(cardTemplateDTO.createdAt(), createdCardTemplateDTO.createdAt());
        assertEquals(cardTemplateDTO.updatedAt(), createdCardTemplateDTO.updatedAt());
        assertEquals(cardTemplateDTO.createdById(), createdCardTemplateDTO.createdById());
    }

    @Test
    void shouldDeleteWhenDeleteById() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(cardTemplateDAO.findById(anyInt())).thenReturn(Optional.of(cardTemplate));
        service.deleteById(CARD_TEMPLATE_ID);

        verify(cardTemplateDAO).delete(cardTemplate);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteByIdNotFound() {
        when(cardTemplateDAO.findById(CARD_TEMPLATE_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.deleteById(CARD_TEMPLATE_ID));
        assertNotNull(thrown);
    }

    @Test
    void shouldReturnCardTemplateDTOWhenGetById() {
        CardTemplateDTO cardTemplateDTO = EntityInitializer.getCardTemplateDTO(1);
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(mapper.toCardTemplateDTO(cardTemplate)).thenReturn(cardTemplateDTO);
        when(cardTemplateDAO.findById(anyInt())).thenReturn(Optional.of(cardTemplate));
        CardTemplateDTO actualCardTemplateDTO = service.getById(cardTemplateDTO.id());

        assertNotNull(actualCardTemplateDTO);
        assertThat(actualCardTemplateDTO).isSameAs(cardTemplateDTO);
        verify(cardTemplateDAO).findById(cardTemplateDTO.id());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetByIdNotFound() {
        when(cardTemplateDAO.findById(CARD_TEMPLATE_ID)).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getById(CARD_TEMPLATE_ID));

        assertNotNull(thrown);
        verify(cardTemplateDAO).findById(CARD_TEMPLATE_ID);
    }

    @Test
    void shouldUpdateByIdWhenUpdate() {
        CardTemplateCreationUpdateDTO cardTemplateUpdateDTO = EntityInitializer.getCardTemplateCreationUpdateDTO();
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(cardTemplateDAO.findById(anyInt())).thenReturn(Optional.of(cardTemplate));
        when(cardTemplateDAO.save(cardTemplate)).thenReturn(cardTemplate);
        service.updateById(cardTemplate.getId(), cardTemplateUpdateDTO);

        assertEquals(cardTemplateUpdateDTO.type(), cardTemplate.getType().toString());
        assertEquals(cardTemplateUpdateDTO.tariff().serviceCost(), cardTemplate.getServiceCost());
        assertEquals(cardTemplateUpdateDTO.tariff().reissueCost(), cardTemplate.getReissueCost());
        verify(cardTemplateDAO).findById(CARD_TEMPLATE_ID);
        verify(cardTemplateDAO).save(cardTemplate);
    }

    @Test
    void shouldReturn404WhenUpdateNotFound() {
        CardTemplateCreationUpdateDTO cardTemplateUpdateDTO = EntityInitializer.getCardTemplateCreationUpdateDTO();

        when(cardTemplateDAO.findById(CARD_TEMPLATE_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.updateById(CARD_TEMPLATE_ID, cardTemplateUpdateDTO));
        assertNotNull(thrown);
        verify(cardTemplateDAO).findById(CARD_TEMPLATE_ID);
    }
}
