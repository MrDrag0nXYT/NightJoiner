package zxc.mrdrag0nxyt.nightJoiner.config

import net.kyori.adventure.util.Ticks
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import java.time.Duration

class Config(plugin: NightJoiner) : AbstractConfig(plugin, "config.yml") {
    var metricsEnabled = true
        private set
    var isVanishCheckEnabled = true
        private set
    var showInConsole = true
        private set

    var isUpdateCheckEnabled = true
        private set
    var announceUpdateMessages = true
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

    var isTitleEnabled = true
        private set
    var title = ""
        private set
    var subtitle = ""
        private set
    var actionbar = ""
        private set
    var titleFadeIn: Duration = Ticks.duration(10)
        private set
    var titleStay: Duration = Ticks.duration(70)
        private set
    var titleFadeOut: Duration = Ticks.duration(20)
        private set

    init {
        updateConfig()
    }

    /*
    * Checking config values
    */

    override fun updateConfig() {
        metricsEnabled = checkValue("enable-metrics", true)

        databaseType = DatabaseType.fromStringType(
            checkValue("database.type", config.getString("database.type", "SQLITE")!!)
        )
        if (databaseType != DatabaseType.SQLITE) {
            databaseConfig = DatabaseConfigEntity(
                host = checkValue(
                    "database.host",
                    config.getString("database.host", "localhost")!!
                ),
                port = checkValue(
                    "database.port",
                    config.getInt("database.port", 3306)
                ),
                username = checkValue(
                    "database.username",
                    config.getString("database.username", "notavailable")!!
                ),
                password = checkValue(
                    "database.password",
                    config.getString("database.password", "notavailable")!!
                ),
                database = checkValue(
                    "database.database",
                    config.getString("database.database", "nj")!!
                ),
            )
        }

        isUpdateCheckEnabled = checkValue("update-check.enabled", true)
        announceUpdateMessages = checkValue("update-check.announce-on-join", true)

        isVanishCheckEnabled = checkValue("vanish-check", true)

        showInConsole = checkValue("messages.show-in-console", true)
        joinMessageTemplate = checkValue(
            "messages.join",
            listOf(
                "",
                " <#ace1af>+</#ace1af> &#fcfcfc%luckperms_prefix% %player_name% &#fcfcfc%player_text%",
                ""
            )
        )
        quitMessageTemplate = checkValue(
            "messages.quit",
            listOf(
                "",
                " <#d45079>-</#d45079> &#fcfcfc%luckperms_prefix% %player_name% &#fcfcfc%player_text%",
                ""
            )
        )

        defaultJoinMessage = checkValue("messages.default.join", "joined game")
        defaultQuitMessage = checkValue("messages.default.quit", "leaved")

        isMotdEnabled = checkValue("messages.motd.enabled", false)
        motd = checkValue(
            "messages.motd.text",
            listOf(
                "",
                " <#fcfcfc>Welcome, <#745c97>%player_name%</#745c97>!",
                " <#c0c0c0>‣ <#fcfcfc>Your group: %luckperms_prefix%",
                ""
            )
        )

        isTitleEnabled = checkValue("messages.motd.title.enabled", false)
        title = checkValue("messages.motd.title.title", "")
        subtitle = checkValue("messages.motd.title.subtitle", "")
        actionbar = checkValue("messages.motd.title.actionbar", "")
        titleFadeIn = Ticks.duration(checkValue("messages.motd.title.time.fade-in", 10))
        titleFadeIn = Ticks.duration(checkValue("messages.motd.title.time.stay", 70))
        titleFadeOut = Ticks.duration(checkValue("messages.motd.title.time.fade-out", 20))

        save()
    }
}
