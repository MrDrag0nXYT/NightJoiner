package zxc.MrDrag0nXYT.nightJoiner.util.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.YamlConfiguration;
import zxc.MrDrag0nXYT.nightJoiner.NightJoiner;
import zxc.MrDrag0nXYT.nightJoiner.util.config.Config;
import zxc.MrDrag0nXYT.nightJoiner.util.database.impl.SQLiteDatabaseWorker;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private final NightJoiner plugin;
    private HikariDataSource dataSource;
    private DatabaseWorker databaseWorker;

    public DatabaseManager(NightJoiner plugin, Config config) {
        this.plugin = plugin;
        YamlConfiguration pluginConfig = config.getConfig();

        updateConnection(pluginConfig);
    }

    public void updateConnection(YamlConfiguration pluginConfig) {
        closeConnection();

        HikariConfig hikariConfig = new HikariConfig();

        switch (pluginConfig.getString("database.type", "sqlite").toLowerCase()) {
            // todo mysql, ...

            default -> {
                hikariConfig.setJdbcUrl("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "database.db");
                databaseWorker = new SQLiteDatabaseWorker();
            }

        }

        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DatabaseWorker getDatabaseWorker() {
        return databaseWorker;
    }

    public void closeConnection() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

}
