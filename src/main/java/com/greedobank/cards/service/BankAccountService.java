package com.greedobank.cards.service;

import com.greedobank.cards.dao.BankAccountDAO;
import com.greedobank.cards.exception.InsufficientFundsException;
import com.greedobank.cards.exception.InvalidInputException;
import com.greedobank.cards.exception.NotActiveException;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import static com.greedobank.cards.utils.ResponseMessages.BANK_ACCOUNT_NOT_FOUND;
import static com.greedobank.cards.utils.ResponseMessages.CARD_IS_NOT_ACTIVE;
import static com.greedobank.cards.utils.ResponseMessages.CARD_NUMBER_DOES_NOT_MATCH_BANK_ACCOUNT;
import static com.greedobank.cards.utils.ResponseMessages.CARD_NUMBER_IS_NOT_VALID;
import static com.greedobank.cards.utils.ResponseMessages.CVV_NOT_CORRECT;
import static com.greedobank.cards.utils.ResponseMessages.END_DATE_NOT_CORRECT;
import static com.greedobank.cards.utils.ResponseMessages.NOT_ENOUGH_FUNDS;
import static com.greedobank.cards.utils.ResponseMessages.PIN_NOT_CORRECT;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountDAO bankAccountDAO;
    private final ValidationCardService validationCardService;

    public BankAccount create(BankAccount bankAccount) {
        bankAccountDAO.save(bankAccount);
        return bankAccount;
    }

    public BankAccount getById(int id) {
        return bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(BANK_ACCOUNT_NOT_FOUND.getDescription(), id)));
    }

    public BankAccount replenishBalance(int id, BankAccount bankAccountReplenish) {
        BankAccount bankAccount = bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(BANK_ACCOUNT_NOT_FOUND.getDescription(), id)));
        Card card = bankAccount.getCard();
        BigDecimal balance = bankAccountReplenish.getBalance();

        if (checkForReplenish(card, bankAccountReplenish.getCard().getNumber())) {
            balance = balance.add(bankAccount.getBalance());
        }
        bankAccount.setBalance(balance);
        bankAccountDAO.save(bankAccount);
        return bankAccount;
    }

    public BankAccount withdrawFunds(int id, BankAccount bankAccountWithdraw) {
        BankAccount bankAccount = bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(BANK_ACCOUNT_NOT_FOUND.getDescription(), id)));
        Card card = bankAccount.getCard();
        BigDecimal balance = bankAccountWithdraw.getBalance();

        if (checkForWithdraw(card, bankAccountWithdraw.getCard().getNumber(), bankAccountWithdraw.getCard().getPin())) {
            if (bankAccount.getBalance().compareTo(balance) < 0) {
                throw new InsufficientFundsException(NOT_ENOUGH_FUNDS.getDescription());
            } else {
                balance = bankAccount.getBalance().subtract(balance);
            }
        }
        bankAccount.setBalance(balance);
        bankAccountDAO.save(bankAccount);
        return bankAccount;
    }

    public BankAccount withdrawFundsOnline(int id, BankAccount bankAccountWithdrawOnline) {
        BankAccount bankAccount = bankAccountDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(BANK_ACCOUNT_NOT_FOUND.getDescription(), id)));
        Card card = bankAccount.getCard();
        BigDecimal balance = bankAccountWithdrawOnline.getBalance();

        if (checkForWithdrawOnline(card, bankAccountWithdrawOnline.getCard().getNumber(),
                bankAccountWithdrawOnline.getCard().getPin(), bankAccountWithdrawOnline.getCard().getCvv(),
                DateTimeFormatter.ofPattern("MM/yy").format(bankAccountWithdrawOnline.getCard().getEndDate()))) {
            if (bankAccount.getBalance().compareTo(balance) < 0) {
                throw new InsufficientFundsException(NOT_ENOUGH_FUNDS.getDescription());
            } else {
                balance = bankAccount.getBalance().subtract(balance);
            }
        }
        bankAccount.setBalance(balance);
        bankAccountDAO.save(bankAccount);
        return bankAccount;
    }

    private boolean checkForReplenish(Card card, String cardNumber) {
        if (!validationCardService.check(card).number().validate()) {
            throw new InvalidInputException(CARD_NUMBER_IS_NOT_VALID.getDescription());
        }
        if (!card.isActive()) {
            throw new NotActiveException(CARD_IS_NOT_ACTIVE.getDescription());
        }
        if (!card.getNumber().equals(cardNumber)) {
            throw new NotFoundException(String.format(CARD_NUMBER_DOES_NOT_MATCH_BANK_ACCOUNT.getDescription(),
                    cardNumber));
        }
        return true;
    }

    private boolean checkForWithdraw(Card card, String cardNumber, String pin) {
        if (checkForReplenish(card, cardNumber)) {
            if (!card.getPin().equals(pin)) {
                throw new NotFoundException(String.format(PIN_NOT_CORRECT.getDescription(),
                        pin));
            }
        }
        return true;
    }

    private boolean checkForWithdrawOnline(Card card, String cardNumber, String pin, String cvv, String endDate) {
        if (checkForWithdraw(card, cardNumber, pin)) {
            if (!card.getCvv().equals(cvv)) {
                throw new NotFoundException(String.format(CVV_NOT_CORRECT.getDescription(), cvv));
            }
            if (!DateTimeFormatter.ofPattern("MM/yy").format(card.getEndDate()).equals(endDate)) {
                throw new NotFoundException(String.format(END_DATE_NOT_CORRECT.getDescription(), endDate));
            }
        }
        return true;
    }
}
