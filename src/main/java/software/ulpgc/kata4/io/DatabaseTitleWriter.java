package software.ulpgc.kata4.io;

import software.ulpgc.kata4.model.Title;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DatabaseTitleWriter implements TitleWriter, AutoCloseable {
    private final Connection connection;
    private final PreparedStatement insertStatement;

    private static final String CREATE_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS titles (
            id TEXT PRIMARY KEY,
            title TEXT NOT NULL,
            genre TEXT,
            isAdult BOOLEAN
        )
    """;

    private static final String INSERT_SQL = """
        INSERT INTO titles (id, title, genre, isAdult)
        VALUES (?, ?, ?, ?)
    """;

    public DatabaseTitleWriter(File file) throws SQLException {
        this("jdbc:sqlite:" + file.getAbsolutePath());
    }

    public DatabaseTitleWriter(String connectionUrl) throws SQLException {
        this.connection = DriverManager.getConnection(connectionUrl);
        this.connection.setAutoCommit(false);
        this.insertStatement = prepareDatabase();
    }

    private PreparedStatement prepareDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_SQL);
        }
        return connection.prepareStatement(INSERT_SQL);
    }

    @Override
    public void write(Title title) throws IOException {
        try {
            setInsertStatementParams(title);
            insertStatement.execute();
        } catch (SQLException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private void setInsertStatementParams(Title movie) throws SQLException {
        insertStatement.setObject(1, movie.id());
        insertStatement.setObject(2, movie.title());
        String genreString = movie.genre() != null ? String.join(", ", movie.genre()) : null;
        insertStatement.setObject(3, genreString);
        insertStatement.setBoolean(4, movie.isAdult());
    }

    @Override
    public void close() throws SQLException {
        connection.commit();
    }
}
