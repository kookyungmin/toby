package net.happykoo.toby.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class JdbcContext<T> {
    private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public T executeSelectOneQuery(String sql,
                                   ResultSetStrategy<T> resultSetStrategy,
                                   Object... args) throws SQLException {
        return executeWithSelectOneStatementStrategy(getPreparedStatementStrategy(sql, args), resultSetStrategy);
    }

    public void executeUpdateQuery(String sql, Object... args) throws SQLException {
        executeWithUpdateStatementStrategy(getPreparedStatementStrategy(sql, args));
    }

    public T executeWithSelectOneStatementStrategy(StatementStrategy statementStrategy,
                                                   ResultSetStrategy<T> resultSetStrategy) throws SQLException {
        ResultSet rs = null;

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = statementStrategy.makePreparedStatement(conn)) {

            rs = ps.executeQuery();

            return resultSetStrategy.extractResultSet(rs);
        } catch (Exception e) {
            throw e;
        } finally {
            if (Objects.nonNull(rs)) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw e;
                }
            }
        }
    }

    public void executeWithUpdateStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement ps = strategy.makePreparedStatement(conn)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private StatementStrategy getPreparedStatementStrategy(String sql, Object... args) {
        return c -> {
            PreparedStatement ps = c.prepareStatement(sql);

            int index = 1;
            for (Object arg : args) {
                ps.setObject(index++, arg);
            }

            return ps;
        };
    }
}
