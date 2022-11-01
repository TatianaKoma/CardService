package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.BankAccountDAO;
import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.dto.BankAccountReplenishDTO;
import com.greedobank.cards.dto.BankAccountWithdrawDTO;
import com.greedobank.cards.dto.BankAccountWithdrawOnlineDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.mapper.BankAccountMapper;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BankAccountServiceTest {
    private static final int BANK_ACCOUNT_ID = 1;

    @InjectMocks
    private BankAccountService service;
    @Mock
    private BankAccountDAO bankAccountDAO;
    @Mock
    private BankAccountMapper mapper;
    @Spy
    private ValidationCardService validationCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBankAccountDTOWhenCreate() {
        BankAccountDTO bankAccountDTO = EntityInitializer.getBankAccountDTO(1);
        BankAccountCreationDTO bankAccountCreationDTO = EntityInitializer.getBankAccountCreationDTO(1);
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);

        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);
        when(mapper.toBankAccount(bankAccountCreationDTO)).thenReturn(bankAccount);
        when(mapper.toBankAccountDTO(bankAccount)).thenReturn(bankAccountDTO);
        BankAccountDTO createdBankAccountDTO = service.create(bankAccountCreationDTO);

        assertNotNull(createdBankAccountDTO);
        assertEquals(bankAccountDTO.id(), createdBankAccountDTO.id());
        assertEquals(bankAccountDTO.iban(), createdBankAccountDTO.iban());
        assertEquals(bankAccountDTO.balance(), createdBankAccountDTO.balance());
        assertEquals(bankAccountDTO.currency(), createdBankAccountDTO.currency());
        assertEquals(bankAccountDTO.created_at(), createdBankAccountDTO.created_at());
        assertEquals(bankAccountDTO.active(), createdBankAccountDTO.active());
    }

    @Test
    void shouldReturnBankAccountDTOWhenGetById() {
        BankAccountDTO bankAccountDTO = EntityInitializer.getBankAccountDTO(1);
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);

        when(mapper.toBankAccountDTO(bankAccount)).thenReturn(bankAccountDTO);
        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        BankAccountDTO actualBankAccountDTO = service.getById(bankAccountDTO.id());

        assertNotNull(actualBankAccountDTO);
        assertThat(actualBankAccountDTO).isSameAs(bankAccountDTO);
        verify(bankAccountDAO).findById(bankAccountDTO.id());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetByIdNotFound() {
        when(bankAccountDAO.findById(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getById(BANK_ACCOUNT_ID));

        assertNotNull(thrown);
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
    }

    @Test
    void shouldIncreaseBalanceWhenReplenish() {
        Card card = EntityInitializer.getCard(1);
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);
        BankAccountReplenishDTO bankAccountReplenishWithdrawDTO =
                EntityInitializer.getBankAccountReplenishDTO();
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = new BankAccountGetBalanceDTO(new BigDecimal(1000));

        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);
        when(mapper.toBankAccountGetBalanceDTO(bankAccount)).thenReturn(bankAccountGetBalanceDTO);

        BankAccountGetBalanceDTO actualBankAccountGetBalance = service
                .replenishBalance(BANK_ACCOUNT_ID, bankAccountReplenishWithdrawDTO);
        assertEquals(bankAccountReplenishWithdrawDTO.cardNumber(), card.getNumber());
        assertEquals(bankAccountGetBalanceDTO.balance(), actualBankAccountGetBalance.balance());
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
        verify(bankAccountDAO).save(bankAccount);
    }

    @Test
    void shouldReturn404WhenReplenishNotFound() {
        BankAccountReplenishDTO bankAccountReplenishDTO =
                EntityInitializer.getBankAccountReplenishDTO();
        when(bankAccountDAO.findById(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.replenishBalance(BANK_ACCOUNT_ID, bankAccountReplenishDTO));
        assertNotNull(thrown);
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
    }

    @Test
    void shouldDecreaseBalanceWhenWithdraw() {
        Card card = EntityInitializer.getCard(3);
        BankAccount bankAccount = EntityInitializer.getBankAccount(3);
        BankAccountWithdrawDTO bankAccountWithdrawDTO =
                EntityInitializer.getBankAccountWithdrawDTO();
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = new BankAccountGetBalanceDTO(new BigDecimal(1000));

        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);
        when(mapper.toBankAccountGetBalanceDTO(bankAccount)).thenReturn(bankAccountGetBalanceDTO);

        BankAccountGetBalanceDTO actualBankAccountGetBalance = service
                .withdrawFunds(BANK_ACCOUNT_ID, bankAccountWithdrawDTO);
        assertEquals(bankAccountWithdrawDTO.cardNumber(), card.getNumber());
        assertEquals(bankAccountWithdrawDTO.pin(), card.getPin());
        assertEquals(bankAccountGetBalanceDTO.balance(), actualBankAccountGetBalance.balance());
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
        verify(bankAccountDAO).save(bankAccount);
    }

    @Test
    void shouldReturn404WhenWithdrawNotFound() {
        BankAccountWithdrawDTO bankAccountWithdrawDTO =
                EntityInitializer.getBankAccountWithdrawDTO();
        when(bankAccountDAO.findById(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.withdrawFunds(BANK_ACCOUNT_ID, bankAccountWithdrawDTO));
        assertNotNull(thrown);
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
    }

    @Test
    void shouldDecreaseBalanceWhenWithdrawOnline() {
        Card card = EntityInitializer.getCard(3);
        BankAccount bankAccount = EntityInitializer.getBankAccount(3);
        BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO =
                EntityInitializer.getBankAccountWithdrawOnlineDTO();
        BankAccountGetBalanceDTO bankAccountGetBalanceDTO = new BankAccountGetBalanceDTO(new BigDecimal(1000));

        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);
        when(mapper.toBankAccountGetBalanceDTO(bankAccount)).thenReturn(bankAccountGetBalanceDTO);

        BankAccountGetBalanceDTO actualBankAccountGetBalance = service
                .withdrawFundsOnline(BANK_ACCOUNT_ID, bankAccountWithdrawOnlineDTO);
        assertEquals(bankAccountWithdrawOnlineDTO.cardNumber(), card.getNumber());
        assertEquals(bankAccountWithdrawOnlineDTO.pin(), card.getPin());
        assertEquals(bankAccountWithdrawOnlineDTO.cvv(), card.getCvv());
        assertEquals(bankAccountWithdrawOnlineDTO.endDate(), DateTimeFormatter.ofPattern("MM/yy").format(card.getEndDate()));
        assertEquals(bankAccountGetBalanceDTO.balance(), actualBankAccountGetBalance.balance());
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
        verify(bankAccountDAO).save(bankAccount);
    }

    @Test
    void shouldReturn404WhenWithdrawOnlineNotFound() {
        BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO =
                EntityInitializer.getBankAccountWithdrawOnlineDTO();
        when(bankAccountDAO.findById(BANK_ACCOUNT_ID)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.withdrawFundsOnline(BANK_ACCOUNT_ID, bankAccountWithdrawOnlineDTO));
        assertNotNull(thrown);
        verify(bankAccountDAO).findById(BANK_ACCOUNT_ID);
    }
}
