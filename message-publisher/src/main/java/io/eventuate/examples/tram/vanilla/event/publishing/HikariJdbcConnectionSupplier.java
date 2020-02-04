package io.eventuate.examples.tram.vanilla.event.publishing;

import com.zaxxer.hikari.HikariDataSource;
import io.eventuate.common.jdbc.EventuateSqlException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class HikariJdbcConnectionSupplier implements JdbcConnectionSupplier {

  private HikariDataSource dataSource;

  public HikariJdbcConnectionSupplier(String user, String password, String driver, String url) {
    dataSource = new HikariDataSource();
    dataSource.setUsername(user);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driver);
    dataSource.setJdbcUrl(url);
  }

  @Override
  public void doWithConnection(Consumer<Connection> connectionConsumer) {
    try(Connection connection = dataSource.getConnection()) {
      connectionConsumer.accept(connection);
    } catch (SQLException e) {
      throw new EventuateSqlException(e);
    }
  }
}
