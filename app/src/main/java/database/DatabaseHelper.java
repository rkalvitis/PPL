package database;

import com.example.ppl.BuildConfig;

import java.sql.*;
import java.util.function.Function;

public class DatabaseHelper {
    private static final String url = BuildConfig.DATABASE_CONNECTION_STRING;
    private static final String user = BuildConfig.DATABASE_USER;
    private static final String passw = BuildConfig.DATABASE_PASSWORD;

    public <T> T executeQuery(String sql, Function<ResultSet, T> handler, Object... params) {
        try (Connection connection = DriverManager.getConnection(url, user, passw)) {
            System.out.println(connection.getCatalog());

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return handler.apply(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int executeUpdate(String sql, Object... parameters) {
        try (Connection connection = DriverManager.getConnection(url, user, passw);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
