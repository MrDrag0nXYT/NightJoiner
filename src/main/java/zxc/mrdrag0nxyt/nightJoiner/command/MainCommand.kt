package zxc.mrdrag0nxyt.nightJoiner.command

import net.kyori.adventure.text.TextReplacementConfig
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.Messages
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseWorker
import zxc.mrdrag0nxyt.nightJoiner.util.exception.UserRecordNotFound
import java.sql.SQLException
import java.util.*

class MainCommand(
    private val plugin: NightJoiner,
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
        for (component in messages.mainUsage) {
            commandSender.sendMessage(component)
        }
        return false
    }

    private fun noPermission(commandSender: CommandSender): Boolean {
        for (component in messages.globalNoPermission) {
            commandSender.sendMessage(component)
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
                        val textReplacementConfig = TextReplacementConfig.builder()
                            .match("%player%")
                            .replacement(strings[1])
                            .build()

                        for (component in messages.mainTargetReset) {
                            commandSender.sendMessage(
                                component.replaceText(textReplacementConfig)
                            )
                        }
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    for (component in messages.globalDatabaseError) {
                        commandSender.sendMessage(component)
                    }
                } catch (e: UserRecordNotFound) {
                    val textReplacementConfig = TextReplacementConfig.builder()
                        .match("%player%")
                        .replacement(strings[1])
                        .build()

                    for (component in messages.mainTargetNotFound) {
                        commandSender.sendMessage(
                            component.replaceText(textReplacementConfig)
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
                        val textReplacementConfig = TextReplacementConfig.builder()
                            .match("%player%")
                            .replacement(strings[1])
                            .build()

                        for (component in messages.mainTargetBanned) {
                            commandSender.sendMessage(
                                component.replaceText(textReplacementConfig)
                            )
                        }
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    for (string in messages.globalDatabaseError) {
                        commandSender.sendMessage(string)
                    }
                } catch (e: UserRecordNotFound) {
                    val textReplacementConfig = TextReplacementConfig.builder()
                        .match("%player%")
                        .replacement(strings[1])
                        .build()

                    for (component in messages.mainTargetNotFound) {
                        commandSender.sendMessage(
                            component.replaceText(textReplacementConfig)
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
                        val textReplacementConfig = TextReplacementConfig.builder()
                            .match("%player%")
                            .replacement(strings[1])
                            .build()

                        for (component in messages.mainTargetUnbanned) {
                            commandSender.sendMessage(
                                component.replaceText(textReplacementConfig)
                            )
                        }
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    for (string in messages.globalDatabaseError) {
                        commandSender.sendMessage(string)
                    }
                } catch (e: UserRecordNotFound) {
                    val textReplacementConfig = TextReplacementConfig.builder()
                        .match("%player%")
                        .replacement(strings[1])
                        .build()

                    for (component in messages.mainTargetNotFound) {
                        commandSender.sendMessage(
                            component.replaceText(textReplacementConfig)
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
            commandSender.sendMessage(string)
        }
        return true
    }
}
