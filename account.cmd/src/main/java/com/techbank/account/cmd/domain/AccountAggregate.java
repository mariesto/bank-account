package com.techbank.account.cmd.domain;

import java.util.Date;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositEvent;
import com.techbank.account.common.events.FundsWithdrawEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;

    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        AccountOpenedEvent event = AccountOpenedEvent.builder().id(command.getId()).accountHolder(command.getAccountHolder()).createdDate(new Date())
                                                     .accountType(command.getAccountType()).openingBalance(command.getOpeningBalance()).build();
        raiseEvent(event);
    }

    public void apply(AccountOpenedEvent event){
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("Funds cannot be deposited into closed account");
        }
        if (amount <= 0){
            throw new IllegalStateException("The deposit amount must be greater than 0");
        }
        FundsDepositEvent event = FundsDepositEvent.builder().id(this.id).amount(amount).build();
        raiseEvent(event);
    }

    public void apply(FundsDepositEvent event){
        this.id = event.getId();
        this.balance = event.getAmount();
    }

    public void withdrawFunds(double amount){
        if (!this.active){
            throw new IllegalStateException("Funds cannot be withdraw from closed account");
        }
        FundsWithdrawEvent event = FundsWithdrawEvent.builder().id(this.id).amount(amount).build();
        raiseEvent(event);
    }

    public void apply(FundsWithdrawEvent event){
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount(){
        if (!this.active){
            throw new IllegalStateException("The bank account has already been closed!");
        }
        AccountClosedEvent event = AccountClosedEvent.builder().id(this.id).build();
        raiseEvent(event);
    }

    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }
}
