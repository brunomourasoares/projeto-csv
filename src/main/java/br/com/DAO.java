package br.com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/cadastrocsv";
        String user = "root";
        String password = "Umabemdificil@2025";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado.", e);
        }

        return DriverManager.getConnection(url, user, password);
    }

    public static void insertData(String table, List<String[]> data) throws SQLException {
        if (table == null || table.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da tabela não pode ser nulo ou vazio.");
        }
        String sql = "INSERT INTO " + table + " (nome, email, telefone) VALUES (?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] row : data) {
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public static List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (!isSystemTable(tableName)) {
                    tables.add(tableName);
                }
            }
        }
        return tables;
    }

    public static void exportTableData(String tableName, File file) throws SQLException, IOException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
             BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                writer.write(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.write("#");
                }
            }
            writer.newLine();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.write(rs.getString(i));
                    if (i < columnCount) {
                        writer.write("#");
                    }
                }
                writer.newLine();
            }
        }
    }

    private static boolean isSystemTable(String tableName) {
        return tableName.startsWith("pg_") || tableName.equals("information_schema");
    }
}