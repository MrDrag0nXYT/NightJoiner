package zxc.mrdrag0nxyt.nightJoiner

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import zxc.mrdrag0nxyt.nightJoiner.command.*
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.config.Messages
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.listener.PlayerJoinQuitListener
import zxc.mrdrag0nxyt.nightJoiner.util.UpdateChecker
import java.sql.SQLException

class NightJoiner : JavaPlugin() {
    private var config = Config(this)
    private var messages = Messages(this)

    private var databaseManager: DatabaseManager = DatabaseManager(this, config.databaseType, config.databaseConfig)

    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            logger.severe("PlaceholderAPI not found!")
            Bukkit.getPluginManager().disablePlugin(this)
        }

        val databaseWorker = databaseManager.databaseWorker

        try {
            databaseManager.getConnection().use { connection ->
                databaseWorker?.initTable(connection!!)
            }
        } catch (e: SQLException) {
            logger.severe(e.message)
        }

        if (config.isUpdateCheckEnabled) server.pluginManager.registerEvents(UpdateChecker(this, config), this)
        if (config.metricsEnabled) Metrics(this, 23311)

        getCommand("nightjoiner")?.setExecutor(MainCommand(this, messages, databaseManager))
        getCommand("setjoin")?.setExecutor(SetJoinCommand(this, messages, databaseManager))
        getCommand("setquit")?.setExecutor(SetQuitCommand(this, messages, databaseManager))
        getCommand("resetjoin")?.setExecutor(
            ResetJoinCommand(
                this, messages,
                databaseManager
            )
        )
        getCommand("resetquit")?.setExecutor(ResetQuitCommand(this, messages, databaseManager))

        server.pluginManager.registerEvents(
            PlayerJoinQuitListener(config, databaseManager), this
        )

        sendTitle(true)
    }

    override fun onDisable() {
        databaseManager.closeConnection()
        sendTitle(false)
    }

    fun reload() {
        config.reload()
        messages.reload()
        databaseManager.updateConnection(config.databaseType, config.databaseConfig)
    }

    private fun sendTitle(isEnable: Boolean) {
        val isEnableMessage =
            if (isEnable) "<#ace1af>successfully loaded!" else "<#d45079>successfully unloaded!"

        // Здесь можно было бы сделать через getLogger().info(), но у меня setColor возвращает Component. Зато MiniMessage)))
        val sender = Bukkit.getConsoleSender()
        val miniMessage = MiniMessage.miniMessage()

        sender.sendMessage(miniMessage.deserialize("<#a880ff>NightJoiner ${description.version} <#fcfcfc>by</#fcfcfc> MrDrag0nXYT</#a880ff> $isEnableMessage"))
    }
}
