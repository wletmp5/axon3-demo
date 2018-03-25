package com.learn.account;

import com.learn.coreapi.AccountCreatedEvent;
import com.learn.coreapi.CreateAccountCommand;
import com.learn.coreapi.MoneyWithdrawnEvent;
import com.learn.coreapi.WithDrawMoneyCommand;
import com.learn.exception.OverDraftLimitExceededException;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class AccountTest {

    private FixtureConfiguration<Account> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(Account.class);
    }

    @Test
    public void testCreateAccount() throws Exception {
        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand("1234", 1000))
                .expectEvents(new AccountCreatedEvent("1234", 1000));
    }

    @Test
    public void testWithdrawReasonableAmount() {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithDrawMoneyCommand("1234", 600))
                .expectEvents(new MoneyWithdrawnEvent("1234", 600, -600));
    }

    @Test
    public void testWithdrawAbsurdAmount() {
        fixture.given(new AccountCreatedEvent("1234", 1000))
                .when(new WithDrawMoneyCommand("1234", 10001))
                .expectNoEvents()
                .expectException(OverDraftLimitExceededException.class);
    }

    @Test
    public void testWithdrawTwice() {
        fixture.given(new AccountCreatedEvent("1234", 1000),
                new MoneyWithdrawnEvent("1234", 999, -999))
                .when(new WithDrawMoneyCommand("1234", 2))
                .expectNoEvents()
                .expectException(OverDraftLimitExceededException.class);
    }

}