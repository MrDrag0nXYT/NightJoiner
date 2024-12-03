package zxc.MrDrag0nXYT.nightJoiner.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zxc.MrDrag0nXYT.nightJoiner.NightJoiner;
import zxc.MrDrag0nXYT.nightJoiner.util.Utilities;
import zxc.MrDrag0nXYT.nightJoiner.config.Config;
import zxc.MrDrag0nXYT.nightJoiner.config.Messages;
import zxc.MrDrag0nXYT.nightJoiner.database.DatabaseManager;
import zxc.MrDrag0nXYT.nightJoiner.database.DatabaseWorker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ResetQuitCommand implements CommandExecutor, TabCompleter {

    private final NightJoiner plugin;
    private Config config;
    private Messages messages;
    private DatabaseManager databaseManager;

    public ResetQuitCommand(NightJoiner plugin, Config config, Messages messages, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.config = config;
        this.messages = messages;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        YamlConfiguration messagesConfig = this.messages.getConfig();

        if (!commandSender.hasPermission("nightjoiner.player.setquit")) {
            for (String string : messagesConfig.getStringList("global.no-permission")) {
                commandSender.sendMessage(
                        Utilities.setColor(string)
                );
            }
            return true;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            DatabaseWorker databaseWorker = databaseManager.getDatabaseWorker();

            try (Connection connection = databaseManager.getConnection()) {

                boolean isBlocked = databaseWorker.getBlockStatus(connection, player.getName());

                if (!isBlocked) {
                    databaseWorker.resetQuitMessage(connection, player.getUniqueId(), player.getName());

                    for (String string : messagesConfig.getStringList("resetquit.success")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }

                } else {
                    for (String string : messagesConfig.getStringList("resetquit.blocked")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }
                }

            } catch (SQLException e) {

                /*
                 * По реке плывёт кирпич
                 * Деревянный как стекло
                 * Ну и пусть себе плывёт
                 * Нам не нужен пенопласт
                 */

                e.printStackTrace();
                for (String string : messagesConfig.getStringList("global.database-error")) {
                    commandSender.sendMessage(
                            Utilities.setColor(string)
                    );
                }
            }

        } else {
            for (String string : messagesConfig.getStringList("global.not-player")) {
                commandSender.sendMessage(
                        Utilities.setColor(string)
                );
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
