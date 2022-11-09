package com.greedobank.cards.dao;

import com.greedobank.cards.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountDAO extends JpaRepository<BankAccount, Integer> {
}
