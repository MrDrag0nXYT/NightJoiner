package zxc.mrdrag0nxyt.nightJoiner.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.config.Messages
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessage
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessageWithPlaceholders
import java.sql.SQLException

class SetJoinCommand(
    private val plugin: NightJoiner,
    private val config: Config,
    private val messages: Messages,
    private val databaseManager: DatabaseManager
) :
    CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (!commandSender.hasPermission("nightjoiner.player.setjoin")) {
            for (string in messages.globalNoPermission) {
                commandSender.sendColoredMessage(string)
            }
            return true
        }

        if (strings.isEmpty()) {
            for (string in messages.setJoinUsage) {
                commandSender.sendColoredMessage(string)
            }
            return true
        }

        if (commandSender is Player) {
            val player = commandSender

            val databaseWorker = databaseManager.databaseWorker

            val message = java.lang.String.join(" ", *strings)
            if (message.isEmpty()) {
                for (string in messages.setJoinUsage) {
                    commandSender.sendColoredMessage(string)
                }
            }

            plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
                try {
                    databaseManager.getConnection().use { connection ->
                        val isBlocked = databaseWorker!!.getBlockStatus(connection!!, player.name)
                        if (!isBlocked) {
                            databaseWorker.setJoinMessage(
                                connection,
                                player.uniqueId,
                                player.name,
                                message
                            )

                            for (string in messages.setJoinSuccess) {
                                commandSender.sendColoredMessageWithPlaceholders(string, mapOf("message" to message))
                            }
                        } else {
                            for (string in messages.setJoinBlocked) {
                                commandSender.sendColoredMessage(string)
                            }
                        }
                    }
                } catch (e: SQLException) {
                    e.printStackTrace()
                    for (string in messages.globalDatabaseError) {
                        commandSender.sendColoredMessage(string)
                    }
                }
            })
        } else {
            for (string in messages.globalNotPlayer) {
                commandSender.sendColoredMessage(string)
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
        return listOf()
    }
}
