package com.greedobank.cards.mapper;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.CardDAO;
import com.greedobank.cards.dao.CustomerDAO;
import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BankAccountMapperTest {
    @InjectMocks
    private BankAccountMapper mapper;
    @Mock
    private CustomerDAO customerDAO;
    @Mock
    private CardDAO cardDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBankAccountWhenToBankAccount() {
        Customer customer = EntityInitializer.getCustomer(1);
        Card card = EntityInitializer.getCard(1);
        BankAccountCreationDTO bankAccountCreationDTO = EntityInitializer.getBankAccountCreationDTO(1);
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);

        when(customerDAO.findById(bankAccountCreationDTO.customerId())).thenReturn(Optional.of(customer));
        when(cardDAO.findById(bankAccountCreationDTO.cardId())).thenReturn(Optional.of(card));

        assertEquals(bankAccountCreationDTO.currency(), bankAccount.getCurrency().toString());
        assertEquals(bankAccountCreationDTO.cardId(), bankAccount.getCard().getId());
        assertEquals(bankAccountCreationDTO.customerId(), bankAccount.getCustomer().getId());
    }

    @Test
    void shouldReturnBankAccountDTOWhenToBankAccountDTO() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);
        BankAccountDTO bankAccountDTO = mapper.toBankAccountDTO(bankAccount);

        assertEquals(bankAccount.getId(), bankAccountDTO.id());
        assertEquals(bankAccount.getIban(), bankAccountDTO.iban());
        assertEquals(bankAccount.getBalance(), bankAccountDTO.balance());
        assertEquals(bankAccount.getCurrency().toString(), bankAccountDTO.currency());
        assertEquals(bankAccount.getCreated_at(), bankAccountDTO.created_at());
        assertEquals(bankAccount.isActive(), bankAccountDTO.active());
    }

    @Test
    void shouldReturnBankAccountGetBalanceDTOWhenToBankAccountGetBalanceDTO() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = mapper.toBankAccountGetBalanceDTO(bankAccount);

        assertEquals(bankAccount.getBalance(), bankAccountGetBalanceDTO.balance());
    }
}
