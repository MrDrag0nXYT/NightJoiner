package zxc.mrdrag0nxyt.nightJoiner.listener

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.util.colorize
import zxc.mrdrag0nxyt.nightJoiner.util.isVanished
import java.sql.SQLException

class PlayerJoinQuitListener(
//    private val plugin: NightJoiner,
    private val config: Config,
    private val databaseManager: DatabaseManager
) :
    Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(null)

        if (config.isMotdEnabled) {
            val player = event.player

            config.motd.forEach { str ->
                var formattedString = PlaceholderAPI.setPlaceholders(player, str)
                formattedString = colorize(formattedString)
                player.sendMessage(formattedString)
            }

            if (config.isTitleEnabled) {
                sendTitle(player)
            }
        }

        broadcast(event, config, true)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage(null)
        broadcast(event, config, false)
    }


    private fun sendTitle(player: Player) {
        val title = colorize(PlaceholderAPI.setPlaceholders(player, config.title))
        val subtitle = colorize(PlaceholderAPI.setPlaceholders(player, config.subtitle))
        val actionbar = colorize(PlaceholderAPI.setPlaceholders(player, config.actionbar))

        player.sendTitle(title, subtitle, config.titleFadeIn, config.titleStay, config.titleFadeOut)
        player.sendActionBar(actionbar)
    }

    private fun broadcast(event: PlayerEvent, config: Config, isJoin: Boolean) {
        val isJoinPath = if (isJoin) "join" else "quit"
        val player = event.player

        if (!player.hasPermission("nightjoiner.player.broadcast.$isJoinPath")) return
        if (config.isVanishCheckEnabled) {
            if (isVanished(player)) return
        }

        val eventMessage = getCurrentMessage(isJoin, player)
        val template = if (isJoin) config.joinMessageTemplate else config.quitMessageTemplate

        val messages = template.map { str ->
            var parsed = str.replace("%player_text%", eventMessage)
            parsed = PlaceholderAPI.setPlaceholders(player, parsed)
            colorize(parsed)
        }

        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            for (str in messages) {
                onlinePlayer.sendMessage(str)
            }
        }

        if (config.showInConsole) {
            for (str in messages) {
                Bukkit.getConsoleSender().sendMessage(str)
            }
        }
    }


    private fun getCurrentMessage(isJoin: Boolean, player: Player): String {
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

        return eventMessage!!
    }
}
