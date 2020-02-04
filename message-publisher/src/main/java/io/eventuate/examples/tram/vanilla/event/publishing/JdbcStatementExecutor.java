package io.eventuate.examples.tram.vanilla.event.publishing;

import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateRowMapper;
import io.eventuate.common.jdbc.EventuateSqlException;
import org.apache.commons.lang.NotImplementedException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JdbcStatementExecutor implements EventuateJdbcStatementExecutor {

  private JdbcConnectionSupplier jdbcConnectionSupplier;

  public JdbcStatementExecutor(JdbcConnectionSupplier jdbcConnectionSupplier) {
    this.jdbcConnectionSupplier = jdbcConnectionSupplier;
  }

  @Override
  public int update(String sql, Object... objects) {
    AtomicInteger updatedRows = new AtomicInteger(0);

    jdbcConnectionSupplier.doWithConnection(connection -> {
      try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        for (int i = 1; i <= objects.length; i++) {
          preparedStatement.setObject(i, objects[i - 1]);
        }

        updatedRows.set(preparedStatement.executeUpdate());
      }
      catch (SQLException e) {
        throw new EventuateSqlException(e);
      }
    });

    return updatedRows.get();
  }

  @Override
  public <T> List<T> query(String s, EventuateRowMapper<T> eventuateRowMapper, Object... objects) {
    throw new NotImplementedException();
  }

  @Override
  public List<Map<String, Object>> queryForList(String s, Object... objects) {
    throw new NotImplementedException();
  }
}
