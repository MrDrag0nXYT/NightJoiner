package zxc.mrdrag0nxyt.nightJoiner.database.impl

import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseWorker
import zxc.mrdrag0nxyt.nightJoiner.database.entity.UserRecord
import zxc.mrdrag0nxyt.nightJoiner.util.exception.UserRecordNotFound
import java.sql.Connection
import java.sql.SQLException
import java.util.*

class SQLiteDatabaseWorker : DatabaseWorker {
    @Throws(SQLException::class)
    override fun initTable(connection: Connection) {
        val sql: String = """
                CREATE TABLE IF NOT EXISTS 'messages' (
                    'id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    'username' TEXT NOT NULL UNIQUE,
                    'uuid' TEXT NOT NULL UNIQUE,
                    'join_message' TEXT DEFAULT NULL,
                    'quit_message' TEXT DEFAULT NULL,
                    'is_blocked' INTEGER DEFAULT 0  CHECK("is_blocked" >= 0 AND "is_blocked" <= 1)
                );
                
                """.trimIndent()

        connection.prepareStatement(sql).use {
            it.executeUpdate()
        }
    }


    @Throws(SQLException::class)
    override fun addUserRecord(connection: Connection, userRecord: UserRecord) {
        val sql = "INSERT INTO messages (username, uuid) VALUES (?, ?)"

        connection.prepareStatement(sql).use {
            it.setString(1, userRecord.username)
            it.setString(2, userRecord.uuid.toString())
            it.executeUpdate()
        }
    }

    @Throws(SQLException::class, UserRecordNotFound::class)
    override fun getUser(connection: Connection, uuid: UUID, username: String?): UserRecord {
        val sql = "SELECT * FROM messages WHERE uuid = ? OR username = ?"

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            val resultSet = it.executeQuery()

            if (resultSet.next()) {
                return UserRecord(
                    resultSet.getLong("id"),
                    resultSet.getString("username"),
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("join_message"),
                    resultSet.getString("quit_message"),
                    resultSet.getInt("is_blocked")
                )
            } else {
                throw UserRecordNotFound()
            }
        }
    }


    @Throws(SQLException::class)
    override fun getJoinMessage(connection: Connection, uuid: UUID, username: String?): String? {
        val sql = "SELECT * FROM messages WHERE uuid = ? OR username = ?"

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            val resultSet = it.executeQuery()

            return if (resultSet.next()) {
                resultSet.getString("join_message")
            } else {
                null
            }
        }
    }

    @Throws(SQLException::class)
    override fun getQuitMessage(connection: Connection, uuid: UUID, username: String?): String? {
        val sql = "SELECT * FROM messages WHERE uuid = ? OR username = ?"

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            val resultSet = it.executeQuery()

            return if (resultSet.next()) {
                resultSet.getString("quit_message")
            } else {
                null
            }
        }
    }


    @Throws(SQLException::class)
    override fun setJoinMessage(connection: Connection, uuid: UUID, username: String, newMessage: String) {
        addMessageForUser(connection, uuid, username, newMessage, "join_message")
    }

    @Throws(SQLException::class)
    override fun setQuitMessage(connection: Connection, uuid: UUID, username: String, newMessage: String) {
        addMessageForUser(connection, uuid, username, newMessage, "quit_message")
    }

    @Throws(SQLException::class)
    override fun resetJoinMessage(connection: Connection, uuid: UUID, username: String) {
        resetMessageForUser(connection, uuid, username, "join_message")
    }

    @Throws(SQLException::class)
    override fun resetQuitMessage(connection: Connection, uuid: UUID, username: String) {
        resetMessageForUser(connection, uuid, username, "quit_message")
    }

    @Throws(SQLException::class)
    override fun resetMessages(connection: Connection, uuid: UUID, username: String) {
        resetMessageForUser(connection, uuid, username, "join_message")
        resetMessageForUser(connection, uuid, username, "quit_message")
    }

    @Throws(SQLException::class)
    private fun addMessageForUser(
        connection: Connection,
        uuid: UUID,
        username: String,
        newMessage: String,
        messageType: String
    ) {
        val sql = "INSERT OR REPLACE INTO messages (uuid, username, $messageType) VALUES (?, ?, ?)"

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            it.setString(3, newMessage)

            it.executeUpdate()
        }
    }

    @Throws(SQLException::class)
    private fun resetMessageForUser(connection: Connection, uuid: UUID, username: String, messageType: String) {
        val sql = "INSERT OR REPLACE INTO messages (uuid, username, $messageType) VALUES (?, ?, NULL)"

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            it.executeUpdate()
        }
    }


    @Throws(SQLException::class)
    override fun getBlockStatus(connection: Connection, username: String?): Boolean {
        val sql = "SELECT is_blocked FROM messages WHERE username = ?"

        connection.prepareStatement(sql).use {
            it.setString(1, username)
            val resultSet = it.executeQuery()

            return if (resultSet.next()) {
                resultSet.getInt("is_blocked") == 1
            } else {
                false
            }
        }
    }

    @Throws(SQLException::class)
    override fun getBlockStatus(connection: Connection, uuid: UUID): Boolean {
        val sql = "SELECT is_blocked FROM messages WHERE uuid = ?"

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())

            val resultSet = it.executeQuery()

            return if (resultSet.next()) {
                resultSet.getInt("is_blocked") == 1
            } else {
                false
            }
        }
    }

    @Throws(SQLException::class, UserRecordNotFound::class)
    override fun setBlockStatus(connection: Connection, target: String?, isBlocked: Int) {
        val sql = "UPDATE messages SET is_blocked = ? WHERE username = ?"

        connection.prepareStatement(sql).use {
            it.setInt(1, isBlocked)
            it.setString(2, target)

            val changed = it.executeUpdate()

            if (changed == 0) {
                throw UserRecordNotFound()
            }
        }
    }
}
