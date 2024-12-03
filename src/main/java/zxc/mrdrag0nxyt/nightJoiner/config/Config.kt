package zxc.mrdrag0nxyt.nightJoiner.config

import org.bukkit.configuration.file.YamlConfiguration
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import java.io.File

class Config(private val plugin: NightJoiner) {
    private val fileName = "config.yml"
    private var file = File(plugin.dataFolder, fileName)
    private var config = init()

    var metricsEnabled = true
        private set
    var isVanishCheckEnabled = true
        private set
    var showInConsole = true
        private set

    var databaseType = DatabaseType.SQLITE
        private set
    var databaseConfig: DatabaseConfigEntity? = null
        private set

    var joinMessageTemplate = listOf("")
        private set
    var quitMessageTemplate = listOf("")
        private set

    var defaultJoinMessage = ""
        private set
    var defaultQuitMessage = ""
        private set

    var isMotdEnabled = true
        private set
    var motd = listOf("")
        private set

    init {
        updateConfig()
    }

    private fun extractIfNotExist() {
        if (!file.exists()) {
            plugin.saveResource(fileName, false)
        }
    }

    private fun init(): YamlConfiguration {
        extractIfNotExist()
        return YamlConfiguration.loadConfiguration(file)
    }

    fun reload() {
        extractIfNotExist()
        try {
            config.load(file)
        } catch (e: Exception) {
            plugin.logger.severe(e.toString())
        }
    }

    fun save() {
        try {
            config.save(file)
        } catch (e: Exception) {
            plugin.logger.severe(e.toString())
        }
    }


    /*
    * Checking config values
    */

    private fun <T> checkConfigValue(key: String, value: T): T {
        if (!config.contains(key)) {
            config.set(key, value)
        }
        return value
    }

    private fun updateConfig() {
        metricsEnabled = checkConfigValue("enable-metrics", true)

        databaseType = DatabaseType.fromStringType(
            checkConfigValue("database.type", config.getString("database.type", "SQLITE")!!)
        )
        if (databaseType != DatabaseType.SQLITE) {
            databaseConfig = DatabaseConfigEntity(
                host = checkConfigValue(
                    "database.host",
                    config.getString("database.host", "localhost")!!
                ),
                port = checkConfigValue(
                    "database.port",
                    config.getInt("database.port", 3306)
                ),
                username = checkConfigValue(
                    "database.username",
                    config.getString("database.username", "notavailable")!!
                ),
                password = checkConfigValue(
                    "database.password",
                    config.getString("database.password", "notavailable")!!
                ),
                database = checkConfigValue(
                    "database.name",
                    config.getString("database.database", "ncr")!!
                ),
            )
        }

        isVanishCheckEnabled = checkConfigValue("vanish-check", true)

        showInConsole = checkConfigValue("messages.show-in-console", true)
        joinMessageTemplate = checkConfigValue(
            "messages.join",
            listOf(
                "",
                " <#ace1af>+</#ace1af> &#fcfcfc%luckperms_prefix% %player_name% &#fcfcfc%player_text%",
                ""
            )
        )
        quitMessageTemplate = checkConfigValue(
            "messages.quit",
            listOf(
                "",
                " <#d45079>-</#d45079> &#fcfcfc%luckperms_prefix% %player_name% &#fcfcfc%player_text%",
                ""
            )
        )

        defaultJoinMessage = checkConfigValue("messages.default.join", "joined game")
        defaultQuitMessage = checkConfigValue("messages.default.quit", "leaved")

        isMotdEnabled = checkConfigValue("messages.motd.enabled", false)
        motd = checkConfigValue(
            "messages.motd.text",
            listOf(
                "",
                " <#fcfcfc>Welcome, <#745c97>%player_name%</#745c97>!",
                " <#c0c0c0>‣ <#fcfcfc>Your group: %luckperms_prefix%",
                ""
            )
        )

        save()
    }
}
