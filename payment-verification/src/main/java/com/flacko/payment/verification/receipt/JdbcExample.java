package com.flacko.payment.verification.receipt;

import java.sql.*;

public class JdbcExample {

    // JDBC URL, username, and password of PostgreSQL server
    private static final String URL = "jdbc:mysql://localhost:3306/fp";
    private static final String USER = "fpuser";
    private static final String PASSWORD = "GFmyjF3eN6d5";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Registering the PostgreSQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Opening a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Creating a statement
            statement = connection.createStatement();

            // Executing the query
            String sqlQuery = "SELECT pattern FROM bank_patterns";
            resultSet = statement.executeQuery(sqlQuery);

            // Processing the result set
            while (resultSet.next()) {
                // Retrieve data from the result set
                String pattern = resultSet.getString("pattern");
                // Do something with the data (e.g., print it)
                System.out.println("Pattern: " + pattern);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Closing resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
