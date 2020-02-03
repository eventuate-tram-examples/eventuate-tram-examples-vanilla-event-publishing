package io.eventuate.examples.tram.vanilla.event.publishing;

import io.eventuate.common.id.IdGeneratorImpl;
import io.eventuate.common.jdbc.EventuateCommonJdbcOperations;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.common.jdbc.sqldialect.MySqlDialect;
import io.eventuate.tram.events.common.DefaultDomainEventNameMapping;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisherImpl;
import io.eventuate.tram.messaging.common.DefaultChannelMapping;
import io.eventuate.tram.messaging.common.MessageInterceptor;
import io.eventuate.tram.messaging.producer.MessageProducer;
import io.eventuate.tram.messaging.producer.common.MessageProducerImpl;
import io.eventuate.tram.messaging.producer.common.MessageProducerImplementation;
import io.eventuate.tram.messaging.producer.jdbc.MessageProducerJdbcImpl;

import java.util.Collections;
import java.util.UUID;

public class PublisherMain {
  public static void main(String[] args) {
    String dbUser = System.getenv("DATASOURCE_USERNAME");
    String dbPassword = System.getenv("DATASOURCE_PASSWORD");
    String dbDriver = System.getenv("DATASOURCE_DRIVER_CLASS_NAME");
    String dbUrl = System.getenv("DATASOURCE_URL");

    EventuateCommonJdbcOperations eventuateCommonJdbcOperations =
            new EventuateCommonJdbcOperations(new JdbcStatementExecutor(dbUser, dbPassword, dbDriver, dbUrl));

    MessageProducerImplementation messageProducerImplementation = new MessageProducerJdbcImpl(eventuateCommonJdbcOperations,
            new IdGeneratorImpl(),
            new EventuateSchema(),
            new MySqlDialect().getCurrentTimeInMillisecondsExpression());

    MessageProducer messageProducer = new MessageProducerImpl(new MessageInterceptor[0],
            new DefaultChannelMapping.DefaultChannelMappingBuilder().build(), messageProducerImplementation);

    DomainEventPublisher domainEventPublisher = new DomainEventPublisherImpl(messageProducer, new DefaultDomainEventNameMapping());

    domainEventPublisher.publish(SampleAggregate.class.getName(),
            generateId(),
            Collections.singletonList(new SampleEvent()));

    System.out.println("===============");
    System.out.println("Event published");
    System.out.println("===============");
  }

  private static String generateId() {
    return UUID.randomUUID().toString();
  }

  private static class SampleEvent implements DomainEvent {}
  private static class SampleAggregate {}
}
