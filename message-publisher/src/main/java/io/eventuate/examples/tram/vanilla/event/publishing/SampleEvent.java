package io.eventuate.examples.tram.vanilla.event.publishing;

import io.eventuate.tram.events.common.DomainEvent;

public class SampleEvent implements DomainEvent {
  private String id;

  public SampleEvent(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
