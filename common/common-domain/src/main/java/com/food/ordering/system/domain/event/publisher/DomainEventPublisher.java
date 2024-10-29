package com.food.ordering.system.domain.event.publisher;

public interface DomainEventPublisher<T> {

    void publish(T domainEvent);
}
