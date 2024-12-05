package zxc.mrdrag0nxyt.nightJoiner.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.DatabaseConfigEntity
import zxc.mrdrag0nxyt.nightJoiner.config.DatabaseType
import zxc.mrdrag0nxyt.nightJoiner.database.impl.MySQLDatabaseWorker
import zxc.mrdrag0nxyt.nightJoiner.database.impl.SQLiteDatabaseWorker
import java.io.File
import java.sql.Connection
import java.sql.SQLException
import java.util.*

class DatabaseManager(
    private val plugin: NightJoiner,
    databaseType: DatabaseType,
    databaseConfig: DatabaseConfigEntity?
) {

    private var datasource: HikariDataSource? = null
    var databaseWorker: DatabaseWorker? = null
        private set

    init {
        updateConnection(databaseType, databaseConfig)
    }

    fun updateConnection(databaseType: DatabaseType, databaseConfig: DatabaseConfigEntity?) {
        closeConnection()
        val hikariConfig = HikariConfig()

        val dbTypeFallback = if (databaseConfig == null) DatabaseType.SQLITE else databaseType

        when (dbTypeFallback) {
            DatabaseType.MYSQL -> {
                hikariConfig.apply {
                    jdbcUrl = "jdbc:mysql://${databaseConfig?.host}:${databaseConfig?.port}/${databaseConfig?.database}"
                    username = databaseConfig?.username
                    password = databaseConfig?.password
                }
                databaseWorker = MySQLDatabaseWorker()
            }

            else -> {
                hikariConfig.apply {
                    jdbcUrl = "jdbc:sqlite:${plugin.dataFolder}${File.separator}database.db"
                    driverClassName = "org.sqlite.JDBC"
                }
                databaseWorker = SQLiteDatabaseWorker()
            }
        }
        datasource = HikariDataSource(hikariConfig)
    }

    @Throws(SQLException::class)
    fun getConnection(): Connection? {
        return datasource?.connection
    }

    fun closeConnection() {
        datasource?.close()
        datasource = null
    }
}
