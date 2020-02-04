package io.eventuate.examples.tram.vanilla.event.publishing;

import java.sql.Connection;
import java.util.function.Consumer;

public interface JdbcConnectionSupplier {
  void doWithConnection(Consumer<Connection> connectionConsumer);
}
