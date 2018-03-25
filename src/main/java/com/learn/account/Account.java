package com.learn.account;

import com.learn.coreapi.AccountCreatedEvent;
import com.learn.coreapi.CreateAccountCommand;
import com.learn.coreapi.MoneyWithdrawnEvent;
import com.learn.coreapi.WithDrawMoneyCommand;
import com.learn.exception.OverDraftLimitExceededException;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@NoArgsConstructor
@Aggregate
public class Account {

    @AggregateIdentifier
    private String accountId;
    private int balance;
    private int overdraftLimit;

    @CommandHandler
    public Account(CreateAccountCommand command){
        apply(new AccountCreatedEvent(command.getAccountId(), command.getOverDraftLimit()));
    }

    @CommandHandler
    public void handle(WithDrawMoneyCommand command) throws OverDraftLimitExceededException {
        if(balance + overdraftLimit >= command.getAmount()) {
            apply(new MoneyWithdrawnEvent(accountId, command.getAmount(), balance - command.getAmount()));
        }else {
            throw new OverDraftLimitExceededException();
        }
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.overdraftLimit = event.getOverDraftLimit();
    }

    @EventSourcingHandler
    public void on(MoneyWithdrawnEvent event) {
        this.balance = event.getBalance();
    }

}
