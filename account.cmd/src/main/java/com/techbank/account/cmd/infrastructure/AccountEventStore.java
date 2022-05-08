package com.techbank.account.cmd.infrastructure;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.infrastructure.EventStore;

@Service
public class AccountEventStore implements EventStore {
    @Autowired
    private EventStoreRepository eventStoreRepository;

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;
        for (BaseEvent event : events) {
            version++;
            event.setVersion(version);
            EventModel eventModel = EventModel.builder().timeStamp(new Date()).aggregateIdentifier(aggregateId)
                                         .aggregateType(AccountAggregate.class.getTypeName()).version(version)
                                         .eventType(event.getClass().getTypeName()).eventData(event).build();
            EventModel persistedEvent = eventStoreRepository.save(eventModel);
            if (persistedEvent != null){
                //TODO : produce event using kafka
            }
        }
    }

    @Override
    public List<BaseEvent> getEvent(String aggregateId) {
        List<EventModel> eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (CollectionUtils.isEmpty(eventStream)){
            throw new AggregateNotFoundException("Incorrect account ID provided");
        }
        return eventStream.stream().map(EventModel::getEventData).collect(Collectors.toList());
    }
}
