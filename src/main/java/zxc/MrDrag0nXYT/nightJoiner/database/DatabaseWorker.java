package zxc.MrDrag0nXYT.nightJoiner.database;

import zxc.MrDrag0nXYT.nightJoiner.database.entity.UserRecord;
import zxc.MrDrag0nXYT.nightJoiner.util.exception.UserRecordNotFound;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseWorker {
    void initTable(Connection connection) throws SQLException;

    void addUserRecord(Connection connection, UserRecord userRecord) throws SQLException;

    UserRecord getUser(Connection connection, UUID uuid, String username) throws SQLException, UserRecordNotFound;

    String getJoinMessage(Connection connection, UUID uuid, String username) throws SQLException;
    String getQuitMessage(Connection connection, UUID uuid, String username) throws SQLException;

    void setJoinMessage(Connection connection, UUID uuid, String username, String newMessage) throws SQLException;
    void setQuitMessage(Connection connection, UUID uuid, String username, String newMessage) throws SQLException;

    void resetJoinMessage(Connection connection, UUID uuid, String username) throws SQLException;
    void resetQuitMessage(Connection connection, UUID uuid, String username) throws SQLException;
    void resetMessages(Connection connection, UUID uuid, String username) throws SQLException;

    boolean getBlockStatus(Connection connection, String username) throws SQLException;
    boolean getBlockStatus(Connection connection, UUID uuid) throws SQLException;

    void setBlockStatus(Connection connection, String target, int isBlock) throws SQLException, UserRecordNotFound;
}
