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
import zxc.MrDrag0nXYT.nightJoiner.util.config.Config;
import zxc.MrDrag0nXYT.nightJoiner.util.config.Messages;
import zxc.MrDrag0nXYT.nightJoiner.util.database.DatabaseManager;
import zxc.MrDrag0nXYT.nightJoiner.util.database.DatabaseWorker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SetJoinCommand implements CommandExecutor, TabCompleter {

    private final NightJoiner plugin;
    private Config config;
    private Messages messages;
    private DatabaseManager databaseManager;

    public SetJoinCommand(NightJoiner plugin, Config config, Messages messages, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.config = config;
        this.messages = messages;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        YamlConfiguration messagesConfig = this.messages.getConfig();

        if (!commandSender.hasPermission("nightjoiner.player.setjoin")) {
            for (String string : messagesConfig.getStringList("global.no-permission")) {
                commandSender.sendMessage(
                        Utilities.setColor(string)
                );
            }
            return true;
        }

        if (strings.length == 0) {
            for (String string : messagesConfig.getStringList("setjoin.usage")) {
                commandSender.sendMessage(
                        Utilities.setColor(string)
                );
            }
            return true;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            DatabaseWorker databaseWorker = databaseManager.getDatabaseWorker();

            String message = String.join(" ", strings);
            if (message.isEmpty()) {
                for (String string : messagesConfig.getStringList("setjoin.usage")) {
                    commandSender.sendMessage(
                            Utilities.setColor(string)
                    );
                }
            }

            try (Connection connection = databaseManager.getConnection()) {

                boolean isBlocked = databaseWorker.getBlockStatus(connection, player.getName());

                if (!isBlocked) {
                    databaseWorker.setJoinMessage(
                            connection,
                            player.getUniqueId(),
                            player.getName(),
                            message
                    );

                    for (String string : messagesConfig.getStringList("setjoin.success")) {
                        commandSender.sendMessage(
                                Utilities.setColor(
                                        string
                                                .replace("%message%", message)
                                )
                        );
                    }

                } else {
                    for (String string : messagesConfig.getStringList("setjoin.blocked")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }
                }

            } catch (SQLException e) {
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
