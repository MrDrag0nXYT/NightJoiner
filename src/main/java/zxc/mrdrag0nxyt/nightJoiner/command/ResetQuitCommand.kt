package zxc.mrdrag0nxyt.nightJoiner.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.*
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import java.sql.SQLException

class ResetQuitCommand(
    private val plugin: NightJoiner,
    private val messages: Messages,
    private val databaseManager: DatabaseManager
) :
    CommandExecutor, TabCompleter {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (!commandSender.hasPermission("nightjoiner.player.setquit")) {
            for (component in messages.globalNoPermission) {
                commandSender.sendMessage(component)
            }
            return true
        }

        if (commandSender is Player) {
            val player = commandSender

            val databaseWorker = databaseManager.databaseWorker

            plugin.server.scheduler.runTaskAsynchronously(plugin, Runnable {
                try {
                    databaseManager.getConnection().use { connection ->
                        val isBlocked = databaseWorker!!.getBlockStatus(connection!!, player.name)
                        if (!isBlocked) {
                            databaseWorker.resetQuitMessage(connection, player.uniqueId, player.name)

                            for (component in messages.resetQuitSuccess) {
                                commandSender.sendMessage(component)
                            }
                        } else {
                            for (component in messages.resetQuitBlocked) {
                                commandSender.sendMessage(component)
                            }
                        }
                    }
                } catch (e: SQLException) {
                    /*
                     * По реке плывёт кирпич
                     * Деревянный как стекло
                     * Ну и пусть себе плывёт
                     * Нам не нужен пенопласт
                     */

                    e.printStackTrace()
                    for (component in messages.globalDatabaseError) {
                        commandSender.sendMessage(component)
                    }
                }
            })
        } else {
            for (component in messages.globalNotPlayer) {
                commandSender.sendMessage(component)
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
