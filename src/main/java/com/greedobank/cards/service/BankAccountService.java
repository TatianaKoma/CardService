package com.greedobank.cards.service;

import com.greedobank.cards.dao.BankAccountDAO;
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
import com.greedobank.cards.mapper.BankAccountMapper;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.utils.ResponseMessages;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class
BankAccountService {
    private final BankAccountDAO bankAccountDAO;
    private final BankAccountMapper bankAccountMapper;
    private final ValidationCardService validationCardService;

    public BankAccountDTO create(BankAccountCreationDTO bankAccountCreationDTO) {
        BankAccount bankAccount = bankAccountMapper.toBankAccount(bankAccountCreationDTO);
        bankAccount.setIban(generateIban());
        bankAccount.setBalance(new BigDecimal(0));
        bankAccount.setCreated_at(OffsetDateTime.now());
        bankAccount.setActive(true);
        bankAccountDAO.save(bankAccount);
        return bankAccountMapper.toBankAccountDTO(bankAccount);
    }

    public BankAccountDTO getById(int id) {
        return bankAccountDAO.findById(id)
                .map(bankAccountMapper::toBankAccountDTO)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.BANK_ACCOUNT_NOT_FOUND, id)));
    }

    public BankAccountGetBalanceDTO replenishBalance(int id, BankAccountReplenishDTO bankAccountReplenishDTO) {
        BankAccount bankAccount = bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.BANK_ACCOUNT_NOT_FOUND, id)));
        Card card = bankAccount.getCard();
        BigDecimal balance = bankAccountReplenishDTO.balance();

        if (checkForReplenish(card, bankAccountReplenishDTO.cardNumber())) {
            balance = balance.add(bankAccount.getBalance());
        }
        bankAccount.setBalance(balance);
        bankAccountDAO.save(bankAccount);
        return bankAccountMapper.toBankAccountGetBalanceDTO(bankAccount);
    }

    public BankAccountGetBalanceDTO withdrawFunds(int id, BankAccountWithdrawDTO bankAccountWithdrawDTO) {
        BankAccount bankAccount = bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.BANK_ACCOUNT_NOT_FOUND, id)));
        Card card = bankAccount.getCard();
        BigDecimal balance = bankAccountWithdrawDTO.balance();

        if (checkForWithdraw(card, bankAccountWithdrawDTO.cardNumber(), bankAccountWithdrawDTO.pin())) {
            if (bankAccount.getBalance().compareTo(balance) < 0) {
                throw new InsufficientFundsException(ResponseMessages.NOT_ENOUGH_FUNDS);
            } else {
                balance = bankAccount.getBalance().subtract(balance);
            }
        }
        bankAccount.setBalance(balance);
        bankAccountDAO.save(bankAccount);
        return bankAccountMapper.toBankAccountGetBalanceDTO(bankAccount);
    }

    public BankAccountGetBalanceDTO withdrawFundsOnline(int id, BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO) {
        BankAccount bankAccount = bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.BANK_ACCOUNT_NOT_FOUND, id)));
        Card card = bankAccount.getCard();
        BigDecimal balance = bankAccountWithdrawOnlineDTO.balance();

        if (checkForWithdrawOnline(card, bankAccountWithdrawOnlineDTO.cardNumber(),
                bankAccountWithdrawOnlineDTO.pin(), bankAccountWithdrawOnlineDTO.cvv(),
                bankAccountWithdrawOnlineDTO.endDate())) {
            if (bankAccount.getBalance().compareTo(balance) < 0) {
                throw new InsufficientFundsException(ResponseMessages.NOT_ENOUGH_FUNDS);
            } else {
                balance = bankAccount.getBalance().subtract(balance);
            }
        }
        bankAccount.setBalance(balance);
        bankAccountDAO.save(bankAccount);
        return bankAccountMapper.toBankAccountGetBalanceDTO(bankAccount);
    }

    private boolean checkForReplenish(Card card, String cardNumber) {
        if (!validationCardService.check(card).number().validate()) {
            throw new InvalidInputException(ResponseMessages.CARD_NUMBER_IS_NOT_VALID);
        }
        if (!card.isActive()) {
            throw new NotActiveException(ResponseMessages.CARD_IS_NOT_ACTIVE);
        }
        if (!card.getNumber().equals(cardNumber)) {
            throw new NotFoundException(String.format(ResponseMessages.CARD_NUMBER_DOES_NOT_MATCH_BANK_ACCOUNT,
                    cardNumber));
        }
        return true;
    }

    private boolean checkForWithdraw(Card card, String cardNumber, String pin) {
        if (checkForReplenish(card, cardNumber)) {
            if (!card.getPin().equals(pin)) {
                throw new NotFoundException(String.format(ResponseMessages.PIN_NOT_CORRECT,
                        pin));
            }
        }
        return true;
    }

    private boolean checkForWithdrawOnline(Card card, String cardNumber, String pin, String cvv, String endDate) {
        if (checkForWithdraw(card, cardNumber, pin)) {
            if (!card.getCvv().equals(cvv)) {
                throw new NotFoundException(String.format(ResponseMessages.CVV_NOT_CORRECT, cvv));
            }
            if (!DateTimeFormatter.ofPattern("MM/yy").format(card.getEndDate()).equals(endDate)) {
                throw new NotFoundException(String.format(ResponseMessages.END_DATE_NOT_CORRECT, endDate));
            }
        }
        return true;
    }

    private String generateIban() {
        return new Iban.Builder()
                .countryCode(CountryCode.UA)
                .bankCode(ResponseMessages.BANK_BIN)
                .buildRandom().toString();
    }
}
