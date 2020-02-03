import io.eventuate.examples.tram.vanilla.event.publishing.SampleAggregate;
import io.eventuate.messaging.kafka.basic.consumer.EventuateKafkaConsumerConfigurationProperties;
import io.eventuate.messaging.kafka.consumer.MessageConsumerKafkaImpl;
import io.eventuate.tram.consumer.common.*;
import io.eventuate.tram.consumer.kafka.EventuateTramKafkaMessageConsumer;
import io.eventuate.tram.messaging.common.DefaultChannelMapping;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestEventPublished {
  @Test
  public void testEventPublished() throws Exception {
    String eventId = System.getProperty("eventId");

    MessageConsumerKafkaImpl messageConsumerKafka = new MessageConsumerKafkaImpl(System.getenv("EVENTUATELOCAL_KAFKA_BOOTSTRAP_SERVERS"),
            EventuateKafkaConsumerConfigurationProperties.empty());

    MessageConsumerImplementation messageConsumerImplementation = new EventuateTramKafkaMessageConsumer(messageConsumerKafka);

    MessageConsumerImpl messageConsumer = new MessageConsumerImpl(new DefaultChannelMapping.DefaultChannelMappingBuilder().build(),
            messageConsumerImplementation, new DecoratedMessageHandlerFactory(Collections.singletonList(new MessageHandlerDecorator() {
      @Override
      public int getOrder() {
        return 0;
      }

      @Override
      public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
      }
    })));

    BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    messageConsumer.subscribe("testSubscriber" + generateUUID(), Collections.singleton(SampleAggregate.class.getName()), message -> {
      messages.add(message.getPayload());
    });

    String message = messages.poll(30, TimeUnit.SECONDS);

    Assert.assertNotNull(message);
    Assert.assertTrue(message.contains(eventId));
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }
}
