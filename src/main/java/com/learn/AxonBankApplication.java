package com.learn;

import com.learn.coreapi.CreateAccountCommand;
import com.learn.coreapi.WithDrawMoneyCommand;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.config.EnableAxonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableAxonAutoConfiguration
@SpringBootApplication
public class AxonBankApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext config = SpringApplication.run(AxonBankApplication.class, args);
        CommandBus commandBus = config.getBean(CommandBus.class);
        commandBus.dispatch(asCommandMessage(new CreateAccountCommand("1234", 500)), new CommandCallback<Object, Object>() {

            @Override
            public void onSuccess(CommandMessage<?> commandMessage, Object o) {
                commandBus.dispatch(asCommandMessage(new WithDrawMoneyCommand("1234", 250)));
                commandBus.dispatch(asCommandMessage(new WithDrawMoneyCommand("1234", 251)));
            }

            @Override
            public void onFailure(CommandMessage<?> commandMessage, Throwable throwable) {

            }
        });

//        EventStorageEngine eventStorageEngine = config.getBean(EventStorageEngine.class);
//        eventStorageEngine.readEvents();

    }

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public CommandBus commandBus() {
        return new AsynchronousCommandBus();
    }
}
