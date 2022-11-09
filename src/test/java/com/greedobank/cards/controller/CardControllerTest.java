package com.greedobank.cards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dto.CardCreationDTO;
import com.greedobank.cards.dto.CardDTO;
import com.greedobank.cards.dto.CardResetPinDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.facade.CardFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {
    private static final int CARD_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardFacade cardFacade;

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnCreatedCardWhenCreate() throws Exception {
        String cardRequest = """
                {
                    "firstName": "Tom",
                    "lastName": "Ford",
                     "active": true,
                     "currency": "UAH",
                     "cardTemplateId":1,
                     "customerId": 1
                }
                """;
        CardDTO cardDTO = EntityInitializer.getCardDTO(1);

        when(cardFacade.create(Mockito.any(CardCreationDTO.class))).thenReturn(cardDTO);

        RequestBuilder requestBuilder = post("/api/v1/card")
                .accept(MediaType.APPLICATION_JSON)
                .content(cardRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnCardWhenGetById() throws Exception {
        String cardResponse = """
                {
                    "id": 2,
                    "firstName": "Emma",
                    "lastName": "Grand",
                    "number": "4731179262995095",
                    "pin": "7773",
                    "cvv": "545",
                    "endDate": "09/25",
                    "active": true,
                    "currency": "UAH"
                }
                   """;
        CardDTO cardDTO = EntityInitializer.getCardDTO(2);

        when(cardFacade.getById(CARD_ID)).thenReturn(cardDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/card/{id}", 1).with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(cardResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenGetByIdNotFound() throws Exception {
        String errorResponse = """
                {
                   "reason": "Card with id 1 not found"
                }
                           """;

        doThrow(new NotFoundException("Card with id 1 not found")).when(cardFacade).getById(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/card/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn204WhenUpdatePin() throws Exception {
        CardResetPinDTO cardResetPinDTO = EntityInitializer.getCardResetPinDTO(1);
        ObjectMapper mapper = new ObjectMapper();

        doNothing().when(cardFacade).updatePin(CARD_ID, cardResetPinDTO);

        RequestBuilder requestBuilder = patch("/api/v1/card/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardResetPinDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn204WhenUpdatePinLessThan4Digits() throws Exception {
        CardResetPinDTO cardResetPinDTO = EntityInitializer.getCardResetPinDTO(3);
        ObjectMapper mapper = new ObjectMapper();

        doNothing().when(cardFacade).updatePin(CARD_ID, cardResetPinDTO);

        RequestBuilder requestBuilder = patch("/api/v1/card/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardResetPinDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn204WhenUpdatePinStartsWithZero() throws Exception {
        CardResetPinDTO cardResetPinDTO = new CardResetPinDTO("0101");
        ObjectMapper mapper = new ObjectMapper();

        doNothing().when(cardFacade).updatePin(CARD_ID, cardResetPinDTO);

        RequestBuilder requestBuilder = patch("/api/v1/card/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cardResetPinDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenPinMoreThan4Digits() throws Exception {
        String cardRequest = """
                   {
                       "pin": "12342"
                   }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Pin cannot be more than 4 digits"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/card/{id}", 1).with(csrf())
                        .content(cardRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenPinIsNull() throws Exception {
        String cardRequest = """
                   {
                       "pin": null
                   }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Pin code cannot be null"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/card/{id}", 1).with(csrf())
                        .content(cardRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenUpdatePinNotFound() throws Exception {
        String errorResponse = """
                {
                   "reason": "Card with id 1 not found"
                }
                           """;
        CardResetPinDTO cardResetPinDTO = EntityInitializer.getCardResetPinDTO(1);
        ObjectMapper mapper = new ObjectMapper();

        doThrow(new NotFoundException("Card with id 1 not found"))
                .when(cardFacade).updatePin(CARD_ID, cardResetPinDTO);

        mockMvc.perform(patch("/api/v1/card/{id}", 1).with(csrf())
                        .content(mapper.writeValueAsString(cardResetPinDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }
}
