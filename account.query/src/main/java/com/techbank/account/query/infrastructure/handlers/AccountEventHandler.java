package com.techbank.account.query.infrastructure.handlers;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositEvent;
import com.techbank.account.common.events.FundsWithdrawEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;

@Service
public class AccountEventHandler implements EventHandler {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount bankAccount = BankAccount.builder().id(event.getId()).accountHolder(event.getAccountHolder()).accountType(event.getAccountType())
                                             .balance(event.getOpeningBalance()).createdDate(new Date()).build();
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositEvent event) {
        Optional<BankAccount> bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isPresent()){
            double currentBalance = bankAccount.get().getBalance();
            double finalBalance = currentBalance + event.getAmount();
            bankAccount.get().setBalance(finalBalance);
            accountRepository.save(bankAccount.get());
        }
    }

    @Override
    public void on(FundsWithdrawEvent event) {
        Optional<BankAccount> bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isPresent()){
            double currentBalance = bankAccount.get().getBalance();
            double finalBalance = currentBalance - event.getAmount();
            bankAccount.get().setBalance(finalBalance);
            accountRepository.save(bankAccount.get());
        }
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
