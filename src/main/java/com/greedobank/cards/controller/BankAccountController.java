package com.greedobank.cards.controller;

import com.greedobank.cards.dto.BankAccountCreationDTO;
import com.greedobank.cards.dto.BankAccountDTO;
import com.greedobank.cards.dto.BankAccountGetBalanceDTO;
import com.greedobank.cards.dto.BankAccountReplenishDTO;
import com.greedobank.cards.dto.BankAccountWithdrawDTO;
import com.greedobank.cards.dto.BankAccountWithdrawOnlineDTO;
import com.greedobank.cards.facade.BankAccountFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/account")
@Tag(name = "Bank account", description = "Interaction with bank account")
public class BankAccountController {
    private final BankAccountFacade bankAccountFacade;

    @PostMapping
    @Operation(summary = "Create a bank account", description = "Lets create a bank account")
    @PreAuthorize("hasRole('ADMIN')")
    public BankAccountDTO create(
            @Valid @RequestBody BankAccountCreationDTO bankAccountCreationDTO) {
        return bankAccountFacade.create(bankAccountCreationDTO);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get the bank account by id", description = "Lets get the bank account by id")
    @PreAuthorize("hasRole('ADMIN')")
    public BankAccountDTO getById(@PathVariable("id") int id) {
        return bankAccountFacade.getById(id);
    }

    @PatchMapping(value = "/replenishment/{id}")
    @Operation(summary = "Replenish balance in the bank account", description = "Lets replenish balance in the bank account")
    @PreAuthorize("isAuthenticated()")
    public BankAccountGetBalanceDTO replenishBalance(@PathVariable("id") int id,
                                                     @Valid @RequestBody BankAccountReplenishDTO bankAccountReplenishDTO) {
        return bankAccountFacade.replenishBalance(id, bankAccountReplenishDTO);
    }

    @PatchMapping(value = "/withdrawal/{id}")
    @Operation(summary = "Withdraw funds from the bank account", description = "Lets withdraw funds from the bank account")
    @PreAuthorize("isAuthenticated()")
    public BankAccountGetBalanceDTO withdrawBalance(@PathVariable("id") int id,
                                                    @Valid @RequestBody BankAccountWithdrawDTO bankAccountWithdrawDTO) {
        return bankAccountFacade.withdrawFunds(id, bankAccountWithdrawDTO);
    }

    @PatchMapping(value = "/withdrawal/online/{id}")
    @Operation(summary = "Withdraw funds online from the bank account", description = "Lets withdraw funds online from the bank account")
    @PreAuthorize("isAuthenticated()")
    public BankAccountGetBalanceDTO withdrawOnlineBalance(@PathVariable("id") int id,
                                                          @Valid @RequestBody BankAccountWithdrawOnlineDTO bankAccountWithdrawOnlineDTO) throws ParseException {
        return bankAccountFacade.withdrawFundsOnline(id, bankAccountWithdrawOnlineDTO);
    }
}
