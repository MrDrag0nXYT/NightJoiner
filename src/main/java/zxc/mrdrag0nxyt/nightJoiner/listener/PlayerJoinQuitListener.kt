package zxc.mrdrag0nxyt.nightJoiner.listener

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.util.isVanished
import zxc.mrdrag0nxyt.nightJoiner.util.setColorWithPlaceholders
import java.sql.SQLException

class PlayerJoinQuitListener(
    private val plugin: NightJoiner,
    private val config: Config,
    private val databaseManager: DatabaseManager
) :
    Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(null)

        if (config.isMotdEnabled) {
            val player = event.player

            for (string in config.motd) {
                player.sendMessage(
                    setColorWithPlaceholders(player, string)
                )
            }
        }

        broadcast(event, config, true)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
        broadcast(event, config, false)
    }

    private fun broadcast(event: PlayerEvent, config: Config, isJoin: Boolean) {
        val isJoinPath = if (isJoin) "join" else "quit"
        val player = event.player

        if (config.isVanishCheckEnabled) {
            if (isVanished(player)) {
                return
            }
        }

        if (player.hasPermission("nightjoiner.player.broadcast.$isJoinPath")) {
            val databaseWorker = databaseManager.databaseWorker

            var eventMessage: String? = null

            try {
                databaseManager.getConnection().use { connection ->
                    eventMessage = if (isJoin) {
                        databaseWorker!!.getJoinMessage(connection!!, player.uniqueId, player.name)
                    } else {
                        databaseWorker!!.getQuitMessage(connection!!, player.uniqueId, player.name)
                    }
                }
            } catch (e: SQLException) {
                Bukkit.getLogger().severe(e.toString())
            }

            if (eventMessage == null) {
                eventMessage = if (isJoin) config.defaultJoinMessage else config.defaultQuitMessage
            }

            val messages: MutableList<Component> = ArrayList()
            val template = if (isJoin) config.joinMessageTemplate else config.quitMessageTemplate

            for (string in template) {
                messages.add(
                    setColorWithPlaceholders(
                        player,
                        string.replace("%player_text%", eventMessage!!)
                    )
                )
            }

            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                for (component in messages) {
                    onlinePlayer.sendMessage(component)
                }
            }

            if (config.showInConsole) {
                for (component in messages) {
                    Bukkit.getConsoleSender().sendMessage(component)
                }
            }
        }
    }
}
