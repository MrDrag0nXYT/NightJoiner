package zxc.MrDrag0nXYT.nightJoiner;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import zxc.MrDrag0nXYT.nightJoiner.command.*;
import zxc.MrDrag0nXYT.nightJoiner.listener.PlayerJoinQuitListener;
import zxc.MrDrag0nXYT.nightJoiner.util.Utilities;
import zxc.MrDrag0nXYT.nightJoiner.config.Config;
import zxc.MrDrag0nXYT.nightJoiner.config.Messages;
import zxc.MrDrag0nXYT.nightJoiner.database.DatabaseManager;
import zxc.MrDrag0nXYT.nightJoiner.database.DatabaseWorker;
import zxc.MrDrag0nXYT.nightJoiner.util.metrics.BStatsMetrics;

import java.sql.Connection;
import java.sql.SQLException;

public final class NightJoiner extends JavaPlugin {

    private Config config;
    private Messages messages;

    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI not found!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        config = new Config(this);
        messages = new Messages(this);

        databaseManager = new DatabaseManager(this, config);
        DatabaseWorker databaseWorker = databaseManager.getDatabaseWorker();

        try (Connection connection = databaseManager.getConnection()) {
            databaseWorker.initTable(connection);
        } catch (SQLException e) {
            getLogger().severe(e.getMessage());
        }

        if (config.getConfig().getBoolean("enable-metrics")) {
            BStatsMetrics metrics = new BStatsMetrics(this, 23311);
        }

        getCommand("nightjoiner").setExecutor(new MainCommand(this, config, messages, databaseManager));
        getCommand("setjoin").setExecutor(new SetJoinCommand(this, config, messages, databaseManager));
        getCommand("setquit").setExecutor(new SetQuitCommand(this, config, messages, databaseManager));
        getCommand("resetjoin").setExecutor(new ResetJoinCommand(this, config, messages, databaseManager));
        getCommand("resetquit").setExecutor(new ResetQuitCommand(this, config, messages, databaseManager));

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this, config, databaseManager), this);

        sendTitle(true);
    }

    @Override
    public void onDisable() {
        databaseManager.closeConnection();
        sendTitle(false);
    }

    public void reload() {
        config.reload();
        messages.reload();
        databaseManager.updateConnection(config.getConfig());
    }

    private void sendTitle(boolean isEnable) {
        String isEnableMessage = isEnable ? "<#ace1af>Plugin successfully loaded!" : "<#d45079>Plugin successfully unloaded!";

        // Здесь можно было бы сделать через getLogger().info(), но у меня setColor возвращает Component. Зато MiniMessage)))

        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        sender.sendMessage(Utilities.setColor(" "));
        sender.sendMessage(Utilities.setColor(" <#a880ff>█▄░█ █ █▀▀ █░█ ▀█▀ ░░█ █▀█ █ █▄░█ █▀▀ █▀█</#a880ff>    <#696969>|</#696969>    <#fcfcfc>Version: <#a880ff>" + this.getDescription().getVersion() + "</#a880ff>"));
        sender.sendMessage(Utilities.setColor(" <#a880ff>█░▀█ █ █▄█ █▀█ ░█░ █▄█ █▄█ █ █░▀█ ██▄ █▀▄</#a880ff>    <#696969>|</#696969>    <#fcfcfc>Author: <#a880ff>MrDrag0nXYT (https://drakoshaslv.ru)</#a880ff>"));
        sender.sendMessage(Utilities.setColor(" "));
        sender.sendMessage(Utilities.setColor(" " + isEnableMessage));
        sender.sendMessage(Utilities.setColor(" "));
    }
}
