package zxc.mrdrag0nxyt.nightJoiner.command

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
//import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.config.Messages
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseWorker
import zxc.mrdrag0nxyt.nightJoiner.util.exception.UserRecordNotFound
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessage
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessageWithPlaceholders
import java.sql.SQLException
import java.util.*

class MainCommand(
    private val plugin: NightJoiner,
//    private val config: Config,
    private val messages: Messages,
    private val databaseManager: DatabaseManager
) :
    CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (strings.isEmpty()) return usage(commandSender)

        val databaseWorker = databaseManager.databaseWorker

        return when (strings[0].lowercase(Locale.getDefault())) {
            "reset" -> reset(commandSender, strings, databaseWorker)
            "ban" -> ban(commandSender, strings, databaseWorker)
            "unban" -> unban(commandSender, strings, databaseWorker)
            "reload" -> reload(commandSender)
            else -> usage(commandSender)
        }
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


    private fun usage(commandSender: CommandSender): Boolean {
        for (string in messages.mainUsage) {
            commandSender.sendColoredMessage(string)
        }
        return false
    }

    private fun noPermission(commandSender: CommandSender): Boolean {
        for (string in messages.globalNoPermission) {
            commandSender.sendColoredMessage(string)
        }
        return false
    }

    private fun reset(
        commandSender: CommandSender,
        strings: Array<String>,
        databaseWorker: DatabaseWorker?
    ): Boolean {
        if (!commandSender.hasPermission("nightjoiner.admin.reset")) return noPermission(commandSender)

        if (strings.size >= 2) {
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
            return true

        } else return usage(commandSender)
    }

    private fun ban(
        commandSender: CommandSender,
        strings: Array<String>,
        databaseWorker: DatabaseWorker?
    ): Boolean {
        if (!commandSender.hasPermission("nightjoiner.admin.ban")) return noPermission(commandSender)

        if (strings.size >= 2) {
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
            return true

        } else return usage(commandSender)
    }

    private fun unban(
        commandSender: CommandSender,
        strings: Array<String>,
        databaseWorker: DatabaseWorker?
    ): Boolean {
        if (!commandSender.hasPermission("nightjoiner.admin.unban")) return noPermission(commandSender)

        if (strings.size >= 2) {
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
            return true

        } else return usage(commandSender)
    }

    private fun reload(commandSender: CommandSender): Boolean {
        if (!commandSender.hasPermission("nightjoiner.admin.reload")) return noPermission(commandSender)

        plugin.reload()
        for (string in messages.mainReloaded) {
            commandSender.sendColoredMessage(string)
        }
        return true
    }
}
