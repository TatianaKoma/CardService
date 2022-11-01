package com.greedobank.cards.controller;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.dto.BankAccountReplenishDTO;
import com.greedobank.cards.dto.BankAccountWithdrawDTO;
import com.greedobank.cards.dto.BankAccountWithdrawOnlineDTO;
import com.greedobank.cards.exception.InsufficientFundsException;
import com.greedobank.cards.exception.InvalidInputException;
import com.greedobank.cards.exception.NotActiveException;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.service.BankAccountService;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountController.class)
class BankAccountControllerTest {
    private static final int BANK_ACCOUNT_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService service;

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnCreatedBankAccountWhenCreate() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "UAH",
                    "cardId": 1,
                    "customerId": 2
                }
                """;
        BankAccountDTO bankAccountDTO = EntityInitializer.getBankAccountDTO(2);

        when(service.create(Mockito.any(BankAccountCreationDTO.class))).thenReturn(bankAccountDTO);

        RequestBuilder requestBuilder = post("/api/v1/account")
                .accept(MediaType.APPLICATION_JSON)
                .content(bankAccountRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenWrongCurrency() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "ttt",
                    "cardId": 1,
                    "customerId": 1
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenCurrencyIsNull() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": null,
                    "cardId": 1,
                    "customerId": 1
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Currency cannot be null"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenCardIdIsNull() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "UAH",
                    "cardId": null,
                    "customerId": 1
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Card id cannot be null"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenCustomerIdIsNull() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "UAH",
                    "cardId": 1,
                    "customerId": null
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                        "Customer id cannot be null"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenCreateAndCardNotFound() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "UAH",
                    "cardId": 5,
                    "customerId": 1
                }
                """;
        String errorResponse = """
                {
                   "reason": "Card with id 5 not found"
                }
                           """;
        BankAccountCreationDTO bankAccountCreationDTO = new BankAccountCreationDTO("UAH", 5, 1);

        doThrow(new NotFoundException("Card with id 5 not found")).when(service).create(bankAccountCreationDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenCreateAndCustomerNotFound() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "UAH",
                    "cardId": 1,
                    "customerId": 10
                }
                """;
        String errorResponse = """
                {
                    "reason": "Customer with id 10 was not found"
                }
                           """;
        BankAccountCreationDTO bankAccountCreationDTO = new BankAccountCreationDTO("UAH", 1, 10);

        doThrow(new NotFoundException("Customer with id 10 was not found")).when(service).create(bankAccountCreationDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenURLIsWrong() throws Exception {
        String bankAccountRequest = """
                {
                    "currency": "UAH",
                    "cardId": 1,
                    "customerId": 1
                }
                """;
        String errorResponse = """
                {
                    "reason": "The requested resource is not found"
                }
                           """;

        mockMvc.perform(post("/api/v1/card").with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnBankAccountWhenGetById() throws Exception {
        String cardResponse = """
                {
                    "id": 3,
                    "iban": "UA374731170949466142867399317",
                    "balance": 2500.00,
                    "currency": "UAH",
                    "created_at": "2022-10-13T22:36:44.021197+03:00",
                    "active": true
                }
                   """;
        BankAccountDTO bankAccountDTO = EntityInitializer.getBankAccountDTO(3);

        when(service.getById(BANK_ACCOUNT_ID)).thenReturn(bankAccountDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/account/{id}", 1).with(csrf())
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
                    "reason": "Bank account with id 11 was not found"
                }
                           """;

        doThrow(new NotFoundException("Bank account with id 11 was not found")).when(service).getById(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/account/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenBalanceIsNegative() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": -1250,
                    "cardNumber":"4731176379502111"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                          "Balance cannot be negative"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/replenishment/{id}", 1).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenBalanceIsNull() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": null,
                    "cardNumber":"4731176379502111"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Validation error",
                    "details": [
                          "Balance cannot be null"
                    ]
                }
                     """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/replenishment/{id}", 1).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenCardNumberIsNotValid() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber":"111111"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Card number is not valid"
                }
                           """;
        BankAccountReplenishDTO bankAccountReplenishDTO =
                new BankAccountReplenishDTO(new BigDecimal(1000), "111111");

        doThrow(new InvalidInputException("Card number is not valid"))
                .when(service).replenishBalance(1, bankAccountReplenishDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/replenishment/{id}", 1).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn400WhenCardIsNotActive() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber":"4731179262995095"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Card is not active"
                }
                           """;
        BankAccountReplenishDTO bankAccountReplenishDTO =
                EntityInitializer.getBankAccountReplenishDTO();

        doThrow(new NotActiveException("Card is not active"))
                .when(service).replenishBalance(1, bankAccountReplenishDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/replenishment/{id}", 1).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturn404WhenCardNumberDoesNotMatchBankAccount() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber":"4731179262995095"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Card number 4731179262995095 does not match bank account"
                }
                           """;
        BankAccountReplenishDTO bankAccountReplenishDTO = EntityInitializer.getBankAccountReplenishDTO();

        doThrow(new NotFoundException("Card number 4731179262995095 does not match bank account"))
                .when(service).replenishBalance(1, bankAccountReplenishDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/replenishment/{id}", 1).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "ADMIN")
    void shouldReturnBalanceWhenReplenish() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber":"4731179262995095"
                }
                """;
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = EntityInitializer.getBankAccountGetBalanceDTO();
        BankAccountReplenishDTO bankAccountReplenishDTO = EntityInitializer.getBankAccountReplenishDTO();

        when(service.replenishBalance(1, bankAccountReplenishDTO)).thenReturn(bankAccountGetBalanceDTO);

        RequestBuilder requestBuilder = patch("/api/v1/account/replenishment/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .content(bankAccountRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturnBalanceWhenWithdraw() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber": "4731174547276064",
                    "pin": "1234"
                }
                """;
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = EntityInitializer.getBankAccountGetBalanceDTO();
        BankAccountWithdrawDTO bankAccountWithdrawDTO = EntityInitializer.getBankAccountWithdrawDTO();

        when(service.withdrawFunds(2, bankAccountWithdrawDTO)).thenReturn(bankAccountGetBalanceDTO);

        RequestBuilder requestBuilder = patch("/api/v1/account/withdrawal/{id}", 2)
                .accept(MediaType.APPLICATION_JSON)
                .content(bankAccountRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturn400WhenWithdrawAndNotEnoughFunds() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber": "4731174547276064",
                    "pin": "1234"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Not enough funds in the account"
                }
                           """;
        BankAccountWithdrawDTO bankAccountWithdrawDTO = EntityInitializer.getBankAccountWithdrawDTO();

        doThrow(new InsufficientFundsException("Not enough funds in the account"))
                .when(service).withdrawFunds(2, bankAccountWithdrawDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/withdrawal/{id}", 2).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturn404WhenWithdrawAndPinNotCorrect() throws Exception {
        String bankAccountRequest = """
                {
                    "balance": 1000,
                    "cardNumber": "4731174547276064",
                    "pin": "1234"
                }
                """;
        String errorResponse = """
                {
                    "reason": "Pin code 1234 does not correct"
                }
                           """;
        BankAccountWithdrawDTO bankAccountWithdrawDTO = EntityInitializer.getBankAccountWithdrawDTO();

        doThrow(new NotFoundException("Pin code 1234 does not correct"))
                .when(service).withdrawFunds(2, bankAccountWithdrawDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/withdrawal/{id}", 2).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturnBalanceWhenWithdrawOnline() throws Exception {
        String bankAccountRequest = """
                {
                      "balance": 100,
                      "cardNumber":"4731174547276064",
                      "pin": "1234",
                      "cvv": "754",
                      "endDate": "10/25"
                }
                """;
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = EntityInitializer.getBankAccountGetBalanceDTO();
        BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO = EntityInitializer.getBankAccountWithdrawOnlineDTO();

        when(service.withdrawFundsOnline(3, bankAccountWithdrawOnlineDTO)).thenReturn(bankAccountGetBalanceDTO);

        RequestBuilder requestBuilder = patch("/api/v1/account/withdrawal/online/{id}", 3)
                .accept(MediaType.APPLICATION_JSON)
                .content(bankAccountRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturn404WhenWithdrawOnlineAndCvvNotCorrect() throws Exception {
        String bankAccountRequest = """
                {
                      "balance": 1000,
                      "cardNumber": "4731174547276064",
                      "pin": "1234",
                      "cvv": "754",
                      "endDate": "09/25"
                }
                """;
        String errorResponse = """
                {
                      "reason": "Cvv code 754 does not correct"
                }
                           """;
        BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO = EntityInitializer.getBankAccountWithdrawOnlineDTO();

        doThrow(new NotFoundException("Cvv code 754 does not correct"))
                .when(service).withdrawFundsOnline(3, bankAccountWithdrawOnlineDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/withdrawal/online/{id}", 3).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }

    @Test
    @WithMockUser(username = "dzhmur@griddynamics.com", roles = "USER")
    void shouldReturn404WhenWithdrawOnlineAndEndDateNotCorrect() throws Exception {
        String bankAccountRequest = """
                {
                      "balance": 1000,
                      "cardNumber": "4731174547276064",
                      "pin": "1234",
                      "cvv": "754",
                      "endDate": "09/25"
                }
                """;
        String errorResponse = """
                {
                      "reason": "Expired date 09/25 does not correct"
                }
                           """;
        BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO = EntityInitializer.getBankAccountWithdrawOnlineDTO();

        doThrow(new NotFoundException("Expired date 09/25 does not correct"))
                .when(service).withdrawFundsOnline(3, bankAccountWithdrawOnlineDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/account/withdrawal/online/{id}", 3).with(csrf())
                        .content(bankAccountRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(errorResponse));
    }
}
