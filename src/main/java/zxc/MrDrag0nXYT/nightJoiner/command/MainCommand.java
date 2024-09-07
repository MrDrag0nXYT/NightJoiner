package zxc.MrDrag0nXYT.nightJoiner.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zxc.MrDrag0nXYT.nightJoiner.NightJoiner;
import zxc.MrDrag0nXYT.nightJoiner.util.Utilities;
import zxc.MrDrag0nXYT.nightJoiner.util.config.Config;
import zxc.MrDrag0nXYT.nightJoiner.util.config.Messages;
import zxc.MrDrag0nXYT.nightJoiner.util.database.DatabaseManager;
import zxc.MrDrag0nXYT.nightJoiner.util.database.DatabaseWorker;
import zxc.MrDrag0nXYT.nightJoiner.util.exception.UserRecordNotFound;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final NightJoiner plugin;
    private Config config;
    private Messages messages;
    private DatabaseManager databaseManager;

    public MainCommand(NightJoiner plugin, Config config, Messages messages, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.config = config;
        this.messages = messages;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        YamlConfiguration messagesConfig = this.messages.getConfig();

        if (strings.length == 0) {
            for (String string : messagesConfig.getStringList("nightjoiner.usage")) {
                commandSender.sendMessage(
                        Utilities.setColor(string)
                );
            }
            return true;
        }

        DatabaseWorker databaseWorker = databaseManager.getDatabaseWorker();

        switch (strings[0].toLowerCase()) {

            case "ban" -> {
                if (commandSender.hasPermission("nightjoiner.admin.ban")) {
                    if (strings.length == 2) {
                        try (Connection connection = databaseManager.getConnection()) {
                            databaseWorker.resetMessages(connection, Bukkit.getOfflinePlayer(strings[1]).getUniqueId(), strings[1]);
                            databaseWorker.setBlockStatus(connection, strings[1], 1);

                            for (String string : messagesConfig.getStringList("nightjoiner.banned")) {
                                commandSender.sendMessage(
                                        Utilities.setColor(
                                                string.replace("%player%", strings[1])
                                        )
                                );
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                            for (String string : messagesConfig.getStringList("global.database-error")) {
                                commandSender.sendMessage(
                                        Utilities.setColor(string)
                                );
                            }

                        } catch (UserRecordNotFound e) {
                            for (String string : messagesConfig.getStringList("nightjoiner.player-not-found")) {
                                commandSender.sendMessage(
                                        Utilities.setColor(
                                                string.replace("%player%", strings[1])
                                        )
                                );
                            }
                        }
                    }

                } else {
                    for (String string : messagesConfig.getStringList("global.no-permission")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }
                }
            }

            case "unban" -> {
                if (commandSender.hasPermission("nightjoiner.admin.unban")) {
                    if (strings.length == 2) {
                        try (Connection connection = databaseManager.getConnection()) {
                            databaseWorker.setBlockStatus(connection, strings[1], 0);

                            for (String string : messagesConfig.getStringList("nightjoiner.unbanned")) {
                                commandSender.sendMessage(
                                        Utilities.setColor(
                                                string.replace("%player%", strings[1])
                                        )
                                );
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                            for (String string : messagesConfig.getStringList("global.database-error")) {
                                commandSender.sendMessage(
                                        Utilities.setColor(string)
                                );
                            }

                        } catch (UserRecordNotFound e) {
                            for (String string : messagesConfig.getStringList("global.player-not-found")) {
                                commandSender.sendMessage(
                                        Utilities.setColor(
                                                string.replace("%player%", strings[1])
                                        )
                                );
                            }
                        }
                    }

                } else {
                    for (String string : messagesConfig.getStringList("global.no-permission")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }
                }
            }

            case "reload" -> {
                if (commandSender.hasPermission("nightjoiner.admin.reload")) {
                    plugin.reload();
                    for (String string : messagesConfig.getStringList("nightjoiner.reloaded")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }

                } else {
                    for (String string : messagesConfig.getStringList("global.no-permission")) {
                        commandSender.sendMessage(
                                Utilities.setColor(string)
                        );
                    }
                }
            }

            default -> {
                for (String string : messagesConfig.getStringList("nightjoiner.usage")) {
                    commandSender.sendMessage(
                            Utilities.setColor(string)
                    );
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("ban", "unban", "reload", "help");
        } else if (strings.length == 2) {
            return null;
        }

        return List.of();
    }
}
