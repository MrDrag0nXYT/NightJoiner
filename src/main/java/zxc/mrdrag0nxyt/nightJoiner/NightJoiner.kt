package zxc.mrdrag0nxyt.nightJoiner

import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import zxc.mrdrag0nxyt.nightJoiner.command.*
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import zxc.mrdrag0nxyt.nightJoiner.config.Messages
import zxc.mrdrag0nxyt.nightJoiner.database.DatabaseManager
import zxc.mrdrag0nxyt.nightJoiner.listener.PlayerJoinQuitListener
import zxc.mrdrag0nxyt.nightJoiner.util.UpdateChecker
import zxc.mrdrag0nxyt.nightJoiner.util.sendColoredMessage
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

        getCommand("nightjoiner")?.setExecutor(MainCommand(this, config, messages, databaseManager))
        getCommand("setjoin")?.setExecutor(SetJoinCommand(this, config, messages, databaseManager))
        getCommand("setquit")?.setExecutor(SetQuitCommand(this, config, messages, databaseManager))
        getCommand("resetjoin")?.setExecutor(
            ResetJoinCommand(
                this, config, messages,
                databaseManager
            )
        )
        getCommand("resetquit")?.setExecutor(ResetQuitCommand(this, config, messages, databaseManager))

        server.pluginManager.registerEvents(
            PlayerJoinQuitListener(
                this,
                config, databaseManager
            ), this
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
            if (isEnable) "<#ace1af>Plugin successfully loaded!" else "<#d45079>Plugin successfully unloaded!"

        // Здесь можно было бы сделать через getLogger().info(), но у меня setColor возвращает Component. Зато MiniMessage)))
        val sender = Bukkit.getConsoleSender()

        sender.sendColoredMessage(" ")
        sender.sendColoredMessage(" <#a880ff>█▄░█ █ █▀▀ █░█ ▀█▀ ░░█ █▀█ █ █▄░█ █▀▀ █▀█</#a880ff>    <#696969>|</#696969>    <#fcfcfc>Version: <#a880ff>${description.version}</#a880ff>")
        sender.sendColoredMessage(" <#a880ff>█░▀█ █ █▄█ █▀█ ░█░ █▄█ █▄█ █ █░▀█ ██▄ █▀▄</#a880ff>    <#696969>|</#696969>    <#fcfcfc>Author: <#a880ff>MrDrag0nXYT ( https://drakoshaslv.ru )</#a880ff>")
        sender.sendColoredMessage(" ")
        sender.sendColoredMessage(" $isEnableMessage")
        sender.sendColoredMessage(" ")
    }
}
