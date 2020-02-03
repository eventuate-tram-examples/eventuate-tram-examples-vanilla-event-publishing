package io.eventuate.examples.tram.vanilla.event.publishing;

import com.zaxxer.hikari.HikariDataSource;
import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateRowMapper;
import io.eventuate.common.jdbc.EventuateSqlException;
import org.apache.commons.lang.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JdbcStatementExecutor implements EventuateJdbcStatementExecutor {

  private HikariDataSource dataSource;

  public JdbcStatementExecutor(String user, String password, String driver, String url) {
    dataSource = new HikariDataSource();
    dataSource.setUsername(user);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driver);
    dataSource.setJdbcUrl(url);
  }

  @Override
  public int update(String sql, Object... objects) {
    try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

      for (int i = 1; i <= objects.length; i++) {
        preparedStatement.setObject(i, objects[i - 1]);
      }

      return preparedStatement.executeUpdate();
    }
    catch (SQLException e) {
      throw new EventuateSqlException(e);
    }
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
