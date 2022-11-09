package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.CardTemplateDAO;
import com.greedobank.cards.exception.NotFoundException;
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
    @InjectMocks
    private CardTemplateService service;

    @Mock
    private CardTemplateDAO cardTemplateDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCardTemplateDTOWhenCreate() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(cardTemplateDAO.save(cardTemplate)).thenReturn(cardTemplate);
        CardTemplate createdCardTemplate = service.create(cardTemplate);

        assertNotNull(createdCardTemplate);
        assertEquals(cardTemplate.getId(), createdCardTemplate.getId());
        assertEquals(cardTemplate.getType(), createdCardTemplate.getType());
        assertEquals(cardTemplate.getIssueCost(), createdCardTemplate.getIssueCost());
        assertEquals(cardTemplate.getServiceCost(), createdCardTemplate.getServiceCost());
        assertEquals(cardTemplate.getReissueCost(), createdCardTemplate.getReissueCost());
        assertEquals(cardTemplate.getCurrency(), createdCardTemplate.getCurrency());
        assertEquals(cardTemplate.getCreatedAt(), createdCardTemplate.getCreatedAt());
        assertEquals(cardTemplate.getUpdatedAt(), createdCardTemplate.getUpdatedAt());
        assertEquals(cardTemplate.getCreatedById(), createdCardTemplate.getCreatedById());
    }

    @Test
    void shouldDeleteWhenDeleteById() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(cardTemplateDAO.findById(anyInt())).thenReturn(Optional.of(cardTemplate));
        service.deleteById(cardTemplate.getId());

        verify(cardTemplateDAO).delete(cardTemplate);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeleteByIdNotFound() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        when(cardTemplateDAO.findById(cardTemplate.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.deleteById(cardTemplate.getId()));
        assertNotNull(thrown);
    }

    @Test
    void shouldReturnCardTemplateWhenGetById() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);

        when(cardTemplateDAO.findById(anyInt())).thenReturn(Optional.of(cardTemplate));
        CardTemplate actualCardTemplate = service.getById(cardTemplate.getId());

        assertNotNull(actualCardTemplate);
        assertThat(actualCardTemplate).isSameAs(cardTemplate);
        verify(cardTemplateDAO).findById(cardTemplate.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetByIdNotFound() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(2);

        when(cardTemplateDAO.findById(cardTemplate.getId())).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getById(cardTemplate.getId()));

        assertNotNull(thrown);
        verify(cardTemplateDAO).findById(cardTemplate.getId());
    }

    @Test
    void shouldUpdateByIdWhenUpdate() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        CardTemplate cardTemplateForUpdate = EntityInitializer.getCardTemplate(2);
        when(cardTemplateDAO.findById(anyInt())).thenReturn(Optional.of(cardTemplate));
        when(cardTemplateDAO.save(cardTemplate)).thenReturn(cardTemplate);
        service.updateById(cardTemplate.getId(), cardTemplateForUpdate);

        assertEquals(cardTemplateForUpdate.getType(), cardTemplate.getType());
        assertEquals(cardTemplateForUpdate.getIssueCost(), cardTemplate.getIssueCost());
        assertEquals(cardTemplateForUpdate.getServiceCost(), cardTemplate.getServiceCost());
        assertEquals(cardTemplateForUpdate.getReissueCost(), cardTemplate.getReissueCost());
        verify(cardTemplateDAO).findById(cardTemplate.getId());
        verify(cardTemplateDAO).save(cardTemplate);
    }

    @Test
    void shouldReturn404WhenUpdateNotFound() {
        CardTemplate cardTemplate = EntityInitializer.getCardTemplate(1);
        CardTemplate cardTemplateForUpdate = EntityInitializer.getCardTemplate(2);

        when(cardTemplateDAO.findById(cardTemplate.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.updateById(cardTemplate.getId(), cardTemplateForUpdate));
        assertNotNull(thrown);
        verify(cardTemplateDAO).findById(cardTemplate.getId());
    }
}
