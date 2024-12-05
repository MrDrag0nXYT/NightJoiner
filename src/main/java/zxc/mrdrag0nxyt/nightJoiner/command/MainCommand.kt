package zxc.mrdrag0nxyt.nightJoiner.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.config.Messages
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.util.exception.UserRecordNotFound
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessage
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessageWithPlaceholders
import java.sql.SQLException
import java.util.*

class MainCommand(
    private val plugin: NightJoiner,
    private val config: Config,
    private val messages: Messages,
    private val databaseManager: DatabaseManager
) :
    CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {

        if (strings.isEmpty()) {
            for (string in messages.mainUsage) {
                commandSender.sendColoredMessage(string)
            }
            return true
        }

        val databaseWorker = databaseManager.databaseWorker

        when (strings[0].lowercase(Locale.getDefault())) {
            "reset" -> {
                if (commandSender.hasPermission("nightjoiner.admin.reset")) {
                    if (strings.size == 2) {
                        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
                            try {
                                databaseManager.getConnection().use { connection ->
                                    databaseWorker?.resetMessages(
                                        connection!!, Bukkit.getOfflinePlayer(strings[1]).uniqueId,
                                        strings[1]
                                    )
                                    for (string in messages.mainTargetReset) {
                                        commandSender.sendColoredMessageWithPlaceholders(
                                            string, mapOf("player" to strings[1])
                                        )
                                    }
                                }
                            } catch (e: SQLException) {
                                e.printStackTrace()
                                for (string in messages.globalDatabaseError) {
                                    commandSender.sendColoredMessage(string)
                                }
                            } catch (e: UserRecordNotFound) {
                                for (string in messages.mainTargetNotFound) {
                                    commandSender.sendColoredMessageWithPlaceholders(
                                        string, mapOf("player" to strings[1])
                                    )
                                }
                            }
                        })
                    }
                } else {
                    for (string in messages.globalNoPermission) {
                        commandSender.sendColoredMessage(string)
                    }
                }
            }

            "ban" -> {
                if (commandSender.hasPermission("nightjoiner.admin.ban")) {
                    if (strings.size == 2) {
                        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
                            try {
                                databaseManager.getConnection().use { connection ->
                                    databaseWorker?.resetMessages(
                                        connection!!, Bukkit.getOfflinePlayer(strings[1]).uniqueId,
                                        strings[1]
                                    )
                                    databaseWorker?.setBlockStatus(connection!!, strings[1], 1)
                                    for (string in messages.mainTargetBanned) {
                                        commandSender.sendColoredMessageWithPlaceholders(
                                            string, mapOf("player" to strings[1])
                                        )
                                    }
                                }
                            } catch (e: SQLException) {
                                e.printStackTrace()
                                for (string in messages.globalDatabaseError) {
                                    commandSender.sendColoredMessage(string)
                                }
                            } catch (e: UserRecordNotFound) {
                                for (string in messages.mainTargetNotFound) {
                                    commandSender.sendColoredMessageWithPlaceholders(
                                        string, mapOf("player" to strings[1])
                                    )
                                }
                            }
                        })
                    }
                } else {
                    for (string in messages.globalNoPermission) {
                        commandSender.sendColoredMessage(string)
                    }
                }
            }

            "unban" -> {
                if (commandSender.hasPermission("nightjoiner.admin.unban")) {
                    if (strings.size == 2) {
                        plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
                            try {
                                databaseManager.getConnection().use { connection ->
                                    databaseWorker!!.setBlockStatus(connection!!, strings[1], 0)
                                    for (string in messages.mainTargetUnbanned) {
                                        commandSender.sendColoredMessageWithPlaceholders(
                                            string, mapOf("player" to strings[1])
                                        )
                                    }
                                }
                            } catch (e: SQLException) {
                                e.printStackTrace()
                                for (string in messages.globalDatabaseError) {
                                    commandSender.sendColoredMessage(string)
                                }
                            } catch (e: UserRecordNotFound) {
                                for (string in messages.mainTargetNotFound) {
                                    commandSender.sendColoredMessageWithPlaceholders(
                                        string, mapOf("player" to strings[1])
                                    )
                                }
                            }
                        })
                    }
                } else {
                    for (string in messages.globalNoPermission) {
                        commandSender.sendColoredMessage(string)
                    }
                }
            }

            "reload" -> {
                if (commandSender.hasPermission("nightjoiner.admin.reload")) {
                    plugin.reload()
                    for (string in messages.mainReloaded) {
                        commandSender.sendColoredMessage(string)
                    }
                } else {
                    for (string in messages.globalNoPermission) {
                        commandSender.sendColoredMessage(string)
                    }
                }
            }

            else -> {
                for (string in messages.mainUsage) {
                    commandSender.sendColoredMessage(string)
                }
            }
        }

        return true
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        s: String,
        strings: Array<String>
    ): List<String>? {
        if (strings.size == 1) {
            val complete = mutableListOf("help")

            if (commandSender.hasPermission("nightjoiner.admin.reload")) complete.add("reload")
            if (commandSender.hasPermission("nightjoiner.admin.reset")) complete.add("reset")
            if (commandSender.hasPermission("nightjoiner.admin.ban")) complete.add("ban")
            if (commandSender.hasPermission("nightjoiner.admin.unban")) complete.add("unban")

            return complete
        } else if (strings.size == 2) {
            return null
        }

        return listOf()
    }
}
