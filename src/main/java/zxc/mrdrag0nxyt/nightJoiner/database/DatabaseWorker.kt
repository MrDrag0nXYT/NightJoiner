package zxc.mrdrag0nxyt.nightJoiner.database

import zxc.mrdrag0nxyt.nightJoiner.database.entity.UserRecord
import zxc.mrdrag0nxyt.nightJoiner.util.exception.UserRecordNotFound
import java.sql.Connection
import java.sql.SQLException
import java.util.*

interface DatabaseWorker {
    @Throws(SQLException::class)
    fun initTable(connection: Connection)

    @Throws(SQLException::class)
    fun addUserRecord(connection: Connection, userRecord: UserRecord)

    @Throws(SQLException::class, UserRecordNotFound::class)
    fun getUser(connection: Connection, uuid: UUID, username: String?): UserRecord

    @Throws(SQLException::class)
    fun getJoinMessage(connection: Connection, uuid: UUID, username: String?): String?

    @Throws(SQLException::class)
    fun getQuitMessage(connection: Connection, uuid: UUID, username: String?): String?

    @Throws(SQLException::class)
    fun setJoinMessage(connection: Connection, uuid: UUID, username: String, newMessage: String)

    @Throws(SQLException::class)
    fun setQuitMessage(connection: Connection, uuid: UUID, username: String, newMessage: String)

    @Throws(SQLException::class)
    fun resetJoinMessage(connection: Connection, uuid: UUID, username: String)

    @Throws(SQLException::class)
    fun resetQuitMessage(connection: Connection, uuid: UUID, username: String)

    @Throws(SQLException::class)
    fun resetMessages(connection: Connection, uuid: UUID, username: String)

    @Throws(SQLException::class)
    fun getBlockStatus(connection: Connection, username: String?): Boolean

    @Throws(SQLException::class)
    fun getBlockStatus(connection: Connection, uuid: UUID): Boolean

    @Throws(SQLException::class, UserRecordNotFound::class)
    fun setBlockStatus(connection: Connection, target: String?, isBlock: Int)
}
