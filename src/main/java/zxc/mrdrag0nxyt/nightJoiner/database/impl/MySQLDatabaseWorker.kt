package zxc.mrdrag0nxyt.nightJoiner.database.impl

import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseWorker
import zxc.mrdrag0nxyt.nightJoiner.database.entity.UserRecord
import zxc.mrdrag0nxyt.nightJoiner.util.exception.UserRecordNotFound
import java.sql.Connection
import java.sql.SQLException
import java.util.*

class MySQLDatabaseWorker : DatabaseWorker {
    @Throws(SQLException::class)
    override fun initTable(connection: Connection) {
        val sql: String = """
                create table if not exists messages (
                    id int auto_increment primary key,
                    username varchar(16) not null unique,
                    uuid varchar(36) not null unique,
                    join_message text default null,
                    quit_message text default null,
                    is_blocked tinyint(1) default 0 check (is_blocked in (0, 1))
                );
                """.trimIndent()

        connection.prepareStatement(sql).use {
            it.executeUpdate()
        }
    }


    @Throws(SQLException::class)
    override fun addUserRecord(connection: Connection, userRecord: UserRecord) {
        val sql = "insert into messages (username, uuid) values (?, ?)"

        connection.prepareStatement(sql).use {
            it.setString(1, userRecord.username)
            it.setString(2, userRecord.uuid.toString())
            it.executeUpdate()
        }
    }

    @Throws(SQLException::class, UserRecordNotFound::class)
    override fun getUser(connection: Connection, uuid: UUID, username: String?): UserRecord {
        val sql = "select * from messages where uuid = ? or username = ?"

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
        val sql = "select * from messages where uuid = ? or username = ?"

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
        val sql = "select * from messages where uuid = ? or username = ?"

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
        val sql = """
            insert into messages (uuid, username, $messageType) values (?, ?, ?)
            on duplicate key update username = values(username), $messageType = values($messageType);
        """.trimIndent()

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            it.setString(3, newMessage)

            it.executeUpdate()
        }
    }

    @Throws(SQLException::class)
    private fun resetMessageForUser(connection: Connection, uuid: UUID, username: String, messageType: String) {
        val sql = """
            insert into messages (uuid, username, $messageType) values (?, ?, null)
            on duplicate key update username = values(username), $messageType = null;
            """.trimIndent()

        connection.prepareStatement(sql).use {
            it.setString(1, uuid.toString())
            it.setString(2, username)
            it.executeUpdate()
        }
    }


    @Throws(SQLException::class)
    override fun getBlockStatus(connection: Connection, username: String?): Boolean {
        val sql = "select is_blocked from messages where username = ?"

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
        val sql = "select is_blocked from messages where uuid = ?"

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
        val sql = "update messages set is_blocked = ? where username = ?"

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
