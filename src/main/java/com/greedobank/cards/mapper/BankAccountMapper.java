package com.greedobank.cards.mapper;

import com.greedobank.cards.dao.CardDAO;
import com.greedobank.cards.dao.CustomerDAO;
import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.exception.NotFoundException;
import com.greedobank.cards.model.BankAccount;
import com.greedobank.cards.model.Card;
import com.greedobank.cards.model.Customer;
import com.greedobank.cards.utils.CurrencyType;
import com.greedobank.cards.utils.ResponseMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankAccountMapper {
    private final CustomerDAO customerDAO;
    private final CardDAO cardDAO;

    public BankAccount toBankAccount(BankAccountCreationDTO bankAccountCreationDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCurrency(CurrencyType.valueOf(bankAccountCreationDTO.currency()));
        Card card = cardDAO.findById(bankAccountCreationDTO.cardId())
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CARD_NOT_FOUND,
                        bankAccountCreationDTO.cardId())));
        bankAccount.setCard(card);
        Customer customer = customerDAO.findById(bankAccountCreationDTO.customerId())
                .orElseThrow(() -> new NotFoundException(String.format(ResponseMessages.CUSTOMER_NOT_FOUND,
                        bankAccountCreationDTO.customerId())));
        bankAccount.setCustomer(customer);
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
