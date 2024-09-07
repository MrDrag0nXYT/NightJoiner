package zxc.MrDrag0nXYT.nightJoiner.util.database.impl;

import zxc.MrDrag0nXYT.nightJoiner.util.database.DatabaseWorker;
import zxc.MrDrag0nXYT.nightJoiner.util.database.entity.UserRecord;
import zxc.MrDrag0nXYT.nightJoiner.util.exception.UserRecordNotFound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLiteDatabaseWorker implements DatabaseWorker {

    @Override
    public void initTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS 'messages' (
                    'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    'username' TEXT NOT NULL UNIQUE,
                    'uuid' TEXT NOT NULL UNIQUE,
                    'join_message' TEXT DEFAULT NULL,
                    'quit_message' TEXT DEFAULT NULL,
                    'is_blocked' INTEGER DEFAULT 0  CHECK("is_blocked" >= 0 AND "is_blocked" <= 1)
                );
                """;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }



    @Override
    public void addUserRecord(Connection connection, UserRecord userRecord) throws SQLException {
        String sql = "INSERT INTO messages (username, uuid) VALUES (?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userRecord.username());
        preparedStatement.setString(2, userRecord.uuid().toString());
        preparedStatement.executeUpdate();
    }

    @Override
    public UserRecord getUser(Connection connection, UUID uuid, String username) throws SQLException, UserRecordNotFound {
        String sql = "SELECT * FROM messages WHERE uuid = ? OR username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new UserRecord(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("join_message"),
                    resultSet.getString("quit_message"),
                    resultSet.getInt("is_blocked")
                    );

        } else {
            throw new UserRecordNotFound();
        }
    }



    @Override
    public String getJoinMessage(Connection connection, UUID uuid, String username) throws SQLException {
        String sql = "SELECT * FROM messages WHERE uuid = ? OR username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("join_message");
        } else {
            return null;
        }
    }

    @Override
    public String getQuitMessage(Connection connection, UUID uuid, String username) throws SQLException {
        String sql = "SELECT * FROM messages WHERE uuid = ? OR username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("quit_message");
        } else {
            return null;
        }
    }



    @Override
    public void setJoinMessage(Connection connection, UUID uuid, String username, String newMessage) throws SQLException {
        addMessageForUser(connection, uuid, username, newMessage, "join_message");
    }

    @Override
    public void setQuitMessage(Connection connection, UUID uuid, String username, String newMessage) throws SQLException {
        addMessageForUser(connection, uuid, username, newMessage, "quit_message");
    }

    @Override
    public void resetJoinMessage(Connection connection, UUID uuid, String username) throws SQLException {
        resetMessageForUser(connection, uuid, username, "join_message");
    }

    @Override
    public void resetQuitMessage(Connection connection, UUID uuid, String username) throws SQLException {
        resetMessageForUser(connection, uuid, username, "quit_message");
    }

    @Override
    public void resetMessages(Connection connection, UUID uuid, String username) throws SQLException {
        resetMessageForUser(connection, uuid, username, "join_message");
        resetMessageForUser(connection, uuid, username, "quit_message");
    }

    private void addMessageForUser(Connection connection, UUID uuid, String username, String newMessage, String messageType) throws SQLException {
        String sql = "INSERT OR REPLACE INTO messages (uuid, username, " + messageType + ") VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, newMessage);

        preparedStatement.executeUpdate();
    }

    private void resetMessageForUser(Connection connection, UUID uuid, String username, String messageType) throws SQLException {
        String sql = "INSERT OR REPLACE INTO messages (uuid, username, " + messageType + ") VALUES (?, ?, NULL)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, username);

        preparedStatement.executeUpdate();
    }



    @Override
    public boolean getBlockStatus(Connection connection, String username) throws SQLException {
        String sql = "SELECT is_blocked FROM messages WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("is_blocked") == 1;
        } else {
            return false;
        }
    }

    @Override
    public boolean getBlockStatus(Connection connection, UUID uuid) throws SQLException {
        String sql = "SELECT is_blocked FROM messages WHERE uuid = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid.toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("is_blocked") == 1;
        } else {
            return false;
        }
    }

    @Override
    public void setBlockStatus(Connection connection, String target, int isBlocked) throws SQLException, UserRecordNotFound {
        String Sql = "UPDATE messages SET is_blocked = ? WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setInt(1, isBlocked);
        preparedStatement.setString(2, target);

        int changed = preparedStatement.executeUpdate();

        if (changed == 0) {
            throw new UserRecordNotFound();
        }
    }

}
