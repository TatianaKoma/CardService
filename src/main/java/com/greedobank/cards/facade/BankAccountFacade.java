package com.greedobank.cards.facade;

import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.dto.BankAccountReplenishDTO;
import com.greedobank.cards.dto.BankAccountWithdrawDTO;
import com.greedobank.cards.dto.BankAccountWithdrawOnlineDTO;
import com.greedobank.cards.mapper.BankAccountMapper;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.OffsetDateTime;

import static com.greedobank.cards.utils.ResponseMessages.BANK_BIN;

@Component
@RequiredArgsConstructor
public class BankAccountFacade {
    private final BankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountDTO create(BankAccountCreationDTO bankAccountCreationDTO) {
        BankAccount bankAccount = bankAccountMapper.toBankAccount(bankAccountCreationDTO);
        bankAccount.setIban(generateIban());
        bankAccount.setBalance(new BigDecimal(0));
        bankAccount.setCreated_at(OffsetDateTime.now());
        bankAccount.setActive(true);
        BankAccount createdBankAccount = bankAccountService.create(bankAccount);
        return bankAccountMapper.toBankAccountDTO(createdBankAccount);

    }

    public BankAccountDTO getById(int id) {
        BankAccount bankAccount = bankAccountService.getById(id);
        return bankAccountMapper.toBankAccountDTO(bankAccount);
    }

    public BankAccountGetBalanceDTO replenishBalance(int id, BankAccountReplenishDTO bankAccountReplenishDTO) {
        BankAccount bankAccountReplenish = bankAccountMapper.toBankAccount(bankAccountReplenishDTO);
        BankAccount bankAccount = bankAccountService.replenishBalance(id, bankAccountReplenish);
        return bankAccountMapper.toBankAccountGetBalanceDTO(bankAccount);
    }

    public BankAccountGetBalanceDTO withdrawFunds(int id, BankAccountWithdrawDTO bankAccountWithdrawDTO) {
        BankAccount bankAccountWithdraw = bankAccountMapper.toBankAccount(bankAccountWithdrawDTO);
        BankAccount bankAccount = bankAccountService.withdrawFunds(id,bankAccountWithdraw);
        return bankAccountMapper.toBankAccountGetBalanceDTO(bankAccount);
    }

    public BankAccountGetBalanceDTO withdrawFundsOnline(int id, BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO) throws ParseException {
        BankAccount bankAccountWithdraw = bankAccountMapper.toBankAccount(bankAccountWithdrawOnlineDTO);
        BankAccount bankAccount = bankAccountService.withdrawFundsOnline(id,bankAccountWithdraw);
        return bankAccountMapper.toBankAccountGetBalanceDTO(bankAccount);
    }

    private String generateIban() {
        return new Iban.Builder()
                .countryCode(CountryCode.UA)
                .bankCode(BANK_BIN.getDescription())
                .buildRandom().toString();
    }
}
