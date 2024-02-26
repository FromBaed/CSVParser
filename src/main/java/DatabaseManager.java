import java.sql.*;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(String tableName, List<String> columns) {
        //Composing of SQL request
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + tableName + " (id SERIAL PRIMARY KEY, ";
        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            sqlCreate += columnName + " VARCHAR(255)";
            if (i < columns.size() - 1) {
                sqlCreate += ", ";
            }
        }
        sqlCreate += ");";

        //execution
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCreate);

            String sqlCheck = "SELECT EXISTS (" +
                    "SELECT FROM information_schema.tables " +
                    "WHERE  table_schema = 'public' " +
                    "AND    table_name   = '" + tableName.toLowerCase() + "');";
            try (ResultSet resultSet = statement.executeQuery(sqlCheck)) {
                if (resultSet.next()) {
                    boolean exists = resultSet.getBoolean(1);
                    if (!exists) {
                        System.out.println("The table " + tableName + " wasn't created.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(String tableName, List<String> columns, List<String[]> rows) {
        clearTable(tableName);

        //Composing of SQL request
        String sqlInsert = "INSERT INTO " + tableName + " (";
        for (int i = 0; i < columns.size(); i++) {
            sqlInsert += columns.get(i);
            if (i < columns.size() - 1) {
                sqlInsert += ", ";
            } else {
                sqlInsert += ") VALUES (";
                for (int j = 0; j < columns.size(); j++) {
                    sqlInsert += "?";
                    if (j < columns.size() - 1) {
                        sqlInsert += ", ";
                    }
                }
            }
        }
        sqlInsert += ");";

        //execution
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert)) {
            for (String[] row : rows) {
                if (row.length != columns.size()) {
                    throw new IllegalArgumentException("Quantity of values is not equal quantity of columns.");
                }

                for (int i = 0; i < row.length; i++) {
                    preparedStatement.setString(i + 1, row[i]);
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearTable(String tableName) {
        String sqlDelete = "DELETE FROM " + tableName + ";";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sqlDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
