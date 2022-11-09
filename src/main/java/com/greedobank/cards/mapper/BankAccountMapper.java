package com.greedobank.cards.mapper;

import com.greedobank.cards.dao.CardDAO;
import com.greedobank.cards.dao.CustomerDAO;
import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.dto.BankAccountReplenishDTO;
import com.greedobank.cards.dto.BankAccountWithdrawDTO;
import com.greedobank.cards.dto.BankAccountWithdrawOnlineDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.Customer;
import com.greedobank.cards.utils.CurrencyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;

import static com.greedobank.cards.utils.ResponseMessages.CARD_NOT_FOUND;
import static com.greedobank.cards.utils.ResponseMessages.CUSTOMER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {
    private final CustomerDAO customerDAO;
    private final CardDAO cardDAO;

    public BankAccount toBankAccount(BankAccountCreationDTO bankAccountCreationDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(CurrencyType.valueOf(bankAccountCreationDTO.currency()));
        Card card = cardDAO.findById(bankAccountCreationDTO.cardId())
                .orElseThrow(() -> new NotFoundException(String.format(CARD_NOT_FOUND.getDescription(),
                        bankAccountCreationDTO.cardId())));
        bankAccount.setCard(card);
        Customer customer = customerDAO.findById(bankAccountCreationDTO.customerId())
                .orElseThrow(() -> new NotFoundException(String.format(CUSTOMER_NOT_FOUND.getDescription(),
                        bankAccountCreationDTO.customerId())));
        bankAccount.setCustomer(customer);
        return bankAccount;
    }

    public BankAccount toBankAccount(BankAccountReplenishDTO bankAccountReplenishDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(bankAccountReplenishDTO.balance());
        Card card = new Card();
        card.setNumber(bankAccountReplenishDTO.cardNumber());
        bankAccount.setCard(card);
        return bankAccount;
    }

    public BankAccount toBankAccount(BankAccountWithdrawDTO bankAccountWithdrawDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(bankAccountWithdrawDTO.balance());
        Card card = new Card();
        card.setNumber(bankAccountWithdrawDTO.cardNumber());
        card.setPin(bankAccountWithdrawDTO.pin());
        bankAccount.setCard(card);
        return bankAccount;
    }

    public BankAccount toBankAccount(BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO) throws ParseException {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(bankAccountWithdrawOnlineDTO.balance());
        Card card = new Card();
        card.setNumber(bankAccountWithdrawOnlineDTO.cardNumber());
        card.setPin(bankAccountWithdrawOnlineDTO.pin());
        card.setCvv(bankAccountWithdrawOnlineDTO.cvv());
        SimpleDateFormat parser = new SimpleDateFormat("MM/yy");
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        card.setEndDate(OffsetDateTime.parse(formatter.format(parser.parse(bankAccountWithdrawOnlineDTO.endDate()))));
        bankAccount.setCard(card);
        return bankAccount;
    }
    public BankAccountDTO toBankAccountDTO(BankAccount bankAccount) {
        return new BankAccountDTO(bankAccount.getId(),
                bankAccount.getIban(),
                bankAccount.getBalance(),
                bankAccount.getCurrency().toString(),
                bankAccount.getCreated_at(),
                bankAccount.isActive());
    }

    public BankAccountGetBalanceDTO toBankAccountGetBalanceDTO(BankAccount bankAccount) {
        return new BankAccountGetBalanceDTO(bankAccount.getBalance());
    }
}
