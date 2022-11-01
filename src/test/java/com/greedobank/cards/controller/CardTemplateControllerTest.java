package com.greedobank.cards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.RestControllerTestConfig;
import com.greedobank.cards.dto.CardTemplateCreationDTO;
import com.greedobank.cards.dto.CardTemplateCreationUpdateDTO;
import com.greedobank.cards.dto.CardTemplateDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.service.CardTemplateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@Import(RestControllerTestConfig.class)
@WebMvcTest(CardTemplateController.class)
class CardTemplateControllerTest {
    private static final int CARD_TEMPLATE_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardTemplateService service;

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnCreatedCardTemplateWhenCreate() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        CardTemplateDTO cardTemplateDTO = EntityInitializer.getCardTemplateDTO(1);

        when(service.create(Mockito.any(CardTemplateCreationDTO.class))).thenReturn(cardTemplateDTO);

        RequestBuilder requestBuilder = post("/api/v1/card-template")
                .accept(MediaType.APPLICATION_JSON)
                .content(cardTemplateRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenWrongType() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "yyy",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Type should be one of the types: GOLD, PREMIUM, PLATINUM"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnAppropriateMessageAnd400WhenTypeIsNull() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": null,
                  "active": true,
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        String errorResponse = """
                      {
                    "reason": "Validation error",
                    "details": [
                        "Type cannot be null"
                    ]
                }
                   """;

        mockMvc.perform(post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenAtLeastOneFieldOfTariffIsNegative() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": -15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        CardTemplateDTO cardTemplateDTO = EntityInitializer.getCardTemplateDTO(1);

        Mockito.when(service.create(Mockito.any(CardTemplateCreationDTO.class))).thenReturn(cardTemplateDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/card-template")
                .accept(MediaType.APPLICATION_JSON)
                .content(cardTemplateRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenAtLeastOneFieldOfTariffIsNull() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": null,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        String errorResponse = """
                   {
                    "reason": "Validation error",
                    "details": [
                        "IssueCost cannot be null"
                    ]
                }
                  """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnAppropriateMessageWhenAtLeastOneFieldOfTariffIsNegative() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": -15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "IssueCost cannot be negative"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenAtLeastOneFieldOfTariffHasMoreThan2DigitsOfPrecision() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15.58975,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                       "IssueCost cannot contain more than two digits of precision and more than 6 digits before it"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenWrongCurrency() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "yyy"
                  }
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Currency must be one of the currencies: EUR, CAD, USD, PLN, UAH"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnAppropriateMessageWhenURLIsWrong() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        String errorResponse = """
                {
                    "reason": "The requested resource is not found"
                }
                           """;

        mockMvc.perform(post("/api/v1/card").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenURLIsWrong() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;
        CardTemplateDTO cardTemplateDTO = EntityInitializer.getCardTemplateDTO(1);

        when(service.create(Mockito.any(CardTemplateCreationDTO.class))).thenReturn(cardTemplateDTO);

        RequestBuilder requestBuilder = post("/api/v1/card")
                .accept(MediaType.APPLICATION_JSON)
                .content(cardTemplateRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnCardTemplateWhenGetById() throws Exception {
        String cardTemplateResponse = """
                {
                  "id": 1,
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  },
                  "createdAt": "2022-06-30T10:50:30+01:00",
                  "updatedAt": "2022-06-30T10:50:30+01:00",
                  "createdById": 1
                }
                   """;
        CardTemplateDTO cardTemplateDTO = EntityInitializer.getCardTemplateDTO(1);

        when(service.getById(CARD_TEMPLATE_ID)).thenReturn(cardTemplateDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/card-template/{id}", 1).with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(cardTemplateResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenGetByIdNotFound() throws Exception {
        String errorResponse = """
                {
                   "reason": "Card-template with id 1 not found"
                }
                           """;

        doThrow(new NotFoundException("Card-template with id 1 not found")).when(service).getById(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/card-template/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn204WhenUpdate() throws Exception {
        CardTemplateCreationUpdateDTO cardTemplateCreationUpdateDTO = EntityInitializer.getCardTemplateCreationUpdateDTO();
        doNothing().when(service).updateById(CARD_TEMPLATE_ID, cardTemplateCreationUpdateDTO);
        ObjectMapper mapper = new ObjectMapper();

        RequestBuilder requestBuilder = patch("/api/v1/card-template/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardTemplateCreationUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenUpdateNotFound() throws Exception {
        String errorResponse = """
                {
                   "reason": "Card-template with id 1 not found"
                }
                           """;
        CardTemplateCreationUpdateDTO cardTemplateCreationUpdateDTO = EntityInitializer.getCardTemplateCreationUpdateDTO();
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new NotFoundException("Card-template with id 1 not found"))
                .when(service).updateById(CARD_TEMPLATE_ID, cardTemplateCreationUpdateDTO);

        mockMvc.perform(patch("/api/v1/card-template/{id}", 1).with(csrf())
                        .content(mapper.writeValueAsString(cardTemplateCreationUpdateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn204WhenDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/card-template/{id}", CARD_TEMPLATE_ID).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenDeleteNotFound() throws Exception {
        String errorResponse = """
                {
                   "reason": "Card-template with id 1 not found"
                }
                           """;

        doThrow(new NotFoundException("Card-template with id 1 not found")).when(service).deleteById(CARD_TEMPLATE_ID);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/card-template/{id}", 1).with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    public void shouldReturn401WhenUnauthenticatedUserTryCreate() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/card-template")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardTemplateRequest)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturn403WhenUnauthorizedUserTryCreate() throws Exception {
        String cardTemplateRequest = """
                {
                  "type": "GOLD",
                  "tariff": {
                    "issueCost": 15,
                    "serviceCost": 16,
                    "reissueCost": 17,
                    "currency": "UAH"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/card-template").with(csrf())
                        .content(cardTemplateRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
