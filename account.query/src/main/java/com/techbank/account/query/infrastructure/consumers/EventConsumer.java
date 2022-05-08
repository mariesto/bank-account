package com.techbank.account.query.infrastructure.consumers;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositEvent;
import com.techbank.account.common.events.FundsWithdrawEvent;

public interface EventConsumer {
    void consumer(@Payload AccountOpenedEvent event, Acknowledgment ack);

    void consumer(@Payload FundsDepositEvent event, Acknowledgment ack);

    void consumer(@Payload FundsWithdrawEvent event, Acknowledgment ack);

    void consumer(@Payload AccountClosedEvent event, Acknowledgment ack);
}
