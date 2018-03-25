package com.learn.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

//Commands
class CreateAccountCommand(val accountId : String, val overDraftLimit : Int)
class WithDrawMoneyCommand(@TargetAggregateIdentifier val accountId : String, val amount : Int)

//Events
class AccountCreatedEvent(val accountId : String, val overDraftLimit : Int)
class MoneyWithdrawnEvent(val accountId: String, val amount: Int, val balance : Int)