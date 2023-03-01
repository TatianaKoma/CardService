package com.greedobank.cards.service;

import com.greedobank.cards.EntityInitializer;
import com.greedobank.cards.dao.BankAccountDAO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.utils.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static com.greedobank.cards.EntityInitializer.getCard;
import static com.greedobank.cards.EntityInitializer.getCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BankAccountServiceTest {
    @InjectMocks
    private BankAccountService service;

    @Mock
    private BankAccountDAO bankAccountDAO;

    @Spy
    private ValidationCardService validationCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnBankAccountWhenCreate() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);

        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);
        BankAccount createdBankAccount = service.create(bankAccount);

        assertNotNull(createdBankAccount);
        assertEquals(bankAccount.getId(), createdBankAccount.getId());
        assertEquals(bankAccount.getIban(), createdBankAccount.getIban());
        assertEquals(bankAccount.getBalance(), createdBankAccount.getBalance());
        assertEquals(bankAccount.getCurrency(), createdBankAccount.getCurrency());
        assertEquals(bankAccount.getCreated_at(), createdBankAccount.getCreated_at());
        assertEquals(bankAccount.isActive(), createdBankAccount.isActive());
    }

    @Test
    void shouldReturnBankAccountWhenGetById() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);

        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        BankAccount actualBankAccount = service.getById(bankAccount.getId());

        assertNotNull(actualBankAccount);
        assertThat(actualBankAccount).isSameAs(bankAccount);
        verify(bankAccountDAO).findById(bankAccount.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGetByIdNotFound() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);

        when(bankAccountDAO.findById(bankAccount.getId())).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getById(bankAccount.getId()));

        assertNotNull(thrown);
        verify(bankAccountDAO).findById(bankAccount.getId());
    }

    @Test
    void shouldIncreaseBalanceWhenReplenish() {
        Card card = getCard(1);
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);
        BankAccount newBankAccount = new BankAccount();
        Card newCard = new Card();
        newCard.setNumber("4731179262995095");
        newBankAccount.setBalance(new BigDecimal(1000));
        newBankAccount.setCard(newCard);
        BankAccount expectedBankAccount = new BankAccount(1, "UA914731171976360791119690404", new BigDecimal(1000), CurrencyType.UAH,
                OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"),
                true, getCard(1), getCustomer(1));

        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);

        BankAccount actualBankAccountGetBalance = service
                .replenishBalance(bankAccount.getId(), newBankAccount);
        assertEquals(newBankAccount.getCard().getNumber(), card.getNumber());
        assertEquals(expectedBankAccount.getBalance(), actualBankAccountGetBalance.getBalance());
        verify(bankAccountDAO).findById(bankAccount.getId());
        verify(bankAccountDAO).save(bankAccount);
    }

    @Test
    void shouldReturn404WhenReplenishNotFound() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(1);
        BankAccount newBankAccount = new BankAccount();
        Card newCard = new Card();
        newCard.setNumber("4731179262995095");
        newBankAccount.setBalance(new BigDecimal(1000));
        newBankAccount.setCard(newCard);

        when(bankAccountDAO.findById(bankAccount.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.replenishBalance(bankAccount.getId(), newBankAccount));
        assertNotNull(thrown);
        verify(bankAccountDAO).findById(bankAccount.getId());
    }

    @Test
    void shouldDecreaseBalanceWhenWithdraw() {
        Card card = getCard(3);
        BankAccount bankAccount = EntityInitializer.getBankAccount(3);
        BankAccount newBankAccount = new BankAccount();
        Card newCard = new Card();
        newCard.setNumber("4731174547276064");
        newCard.setPin("1234");
        newBankAccount.setBalance(new BigDecimal(1000));
        newBankAccount.setCard(newCard);
        BankAccount expectedBankAccount =  new BankAccount(3, "UA914731171976360791119690404", new BigDecimal(1500), CurrencyType.UAH,
                OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"),
                true, getCard(3), getCustomer(3));

        when(bankAccountDAO.findById(bankAccount.getId())).thenReturn(Optional.of(bankAccount));
        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);

        BankAccount actualBankAccountGetBalance = service
                .withdrawFunds(bankAccount.getId(), newBankAccount);
        assertEquals(newBankAccount.getCard().getNumber(), card.getNumber());
        assertEquals(newBankAccount.getCard().getPin(), card.getPin());
        assertEquals(expectedBankAccount.getBalance(), actualBankAccountGetBalance.getBalance());
        verify(bankAccountDAO).findById(bankAccount.getId());
        verify(bankAccountDAO).save(bankAccount);
    }

    @Test
    void shouldReturn404WhenWithdrawNotFound() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(3);
        BankAccount newBankAccount = new BankAccount();
        Card newCard = new Card();
        newCard.setNumber("4731174547276064");
        newCard.setPin("1234");
        newBankAccount.setBalance(new BigDecimal(1000));
        newBankAccount.setCard(newCard);

        when(bankAccountDAO.findById(bankAccount.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.withdrawFunds(bankAccount.getId(), newBankAccount));
        assertNotNull(thrown);
        verify(bankAccountDAO).findById(bankAccount.getId());
    }

    @Test
    void shouldDecreaseBalanceWhenWithdrawOnline() {
        Card card = getCard(3);
        BankAccount bankAccount = EntityInitializer.getBankAccount(3);
        BankAccount newBankAccount = new BankAccount();
        Card newCard = new Card();
        newCard.setNumber("4731174547276064");
        newCard.setPin("1234");
        newCard.setCvv("754");
        newCard.setEndDate(OffsetDateTime.parse("2025-09-30T10:50:30+01:00"));
        newBankAccount.setBalance(new BigDecimal(1000));
        newBankAccount.setCard(newCard);
        BankAccount expectedBankAccount =  new BankAccount(3, "UA914731171976360791119690404", new BigDecimal(1500), CurrencyType.UAH,
                OffsetDateTime.parse("2022-10-14T09:26:51.3090147+03:00"),
                true, getCard(3), getCustomer(3));

        when(bankAccountDAO.findById(anyInt())).thenReturn(Optional.of(bankAccount));
        when(bankAccountDAO.save(bankAccount)).thenReturn(bankAccount);

        BankAccount actualBankAccountGetBalance = service
                .withdrawFundsOnline(bankAccount.getId(),newBankAccount);
        assertEquals(newBankAccount.getCard().getNumber(), card.getNumber());
        assertEquals(newBankAccount.getCard().getPin(), card.getPin());
        assertEquals(newBankAccount.getCard().getCvv(), card.getCvv());
        assertEquals(newBankAccount.getCard().getEndDate(), card.getEndDate());
        assertEquals(expectedBankAccount.getBalance(), actualBankAccountGetBalance.getBalance());
        verify(bankAccountDAO).findById(bankAccount.getId());
        verify(bankAccountDAO).save(bankAccount);
    }

    @Test
    void shouldReturn404WhenWithdrawOnlineNotFound() {
        BankAccount bankAccount = EntityInitializer.getBankAccount(3);
        BankAccount newBankAccount = new BankAccount();
        Card newCard = new Card();
        newCard.setNumber("4731174547276064");
        newCard.setPin("1234");
        newCard.setCvv("754");
        newCard.setEndDate(OffsetDateTime.parse("2025-09-30T10:50:30+01:00"));
        newBankAccount.setBalance(new BigDecimal(1000));
        newBankAccount.setCard(newCard);

        when(bankAccountDAO.findById(bankAccount.getId())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> service.withdrawFundsOnline(bankAccount.getId(), newBankAccount));
        assertNotNull(thrown);
        verify(bankAccountDAO).findById(bankAccount.getId());
    }
}
