package zxc.mrdrag0nxyt.nightJoiner.config

import org.bukkit.configuration.file.YamlConfiguration
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import java.io.File

class Messages(private val plugin: NightJoiner) {
    private val fileName = "messages.yml"
    private var file = File(plugin.dataFolder, fileName)
    private var config = init()

    var globalNoPermission = listOf("")
        private set
    var globalNotPlayer = listOf("")
        private set
    var globalDatabaseError = listOf("")
        private set

    var mainUsage = listOf("")
        private set
    var mainReloaded = listOf("")
        private set
    var mainTargetNotFound = listOf("")
        private set
    var mainTargetBanned = listOf("")
        private set
    var mainTargetUnbanned = listOf("")
        private set
    var mainTargetReset = listOf("")
        private set

    var setJoinUsage = listOf("")
        private set
    var setJoinSuccess = listOf("")
        private set
    var setJoinBlocked = listOf("")
        private set

    var setQuitUsage = listOf("")
        private set
    var setQuitSuccess = listOf("")
        private set
    var setQuitBlocked = listOf("")
        private set

    var resetJoinSuccess = listOf("")
        private set
    var resetJoinBlocked = listOf("")
        private set

    var resetQuitSuccess = listOf("")
        private set
    var resetQuitBlocked = listOf("")
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

    private fun updateConfig() {
        globalNoPermission = checkValue(
            "global.no-permission",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>У вас <#d45079>недостаточно прав</#d45079> для выполнения этого действия",
                ""
            )
        )
        globalNotPlayer = checkValue(
            "global.not-player",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Эта команда <#d45079>недоступна</#d45079> для выполнения из консоли",
                ""
            )
        )
        globalDatabaseError = checkValue(
            "global.database-error",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>При выполнении действия <#d45079>произошла ошибка</#d45079> в базе данных",
                ""
            )
        )

        mainUsage = checkValue(
            "nightjoiner.usage", listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Информация",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner reload'><#745c97>/nightjoiner reload</click> <#c0c0c0>- <#fcfcfc>перезагрузить плагин",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner ban'><#745c97>/nightjoiner ban <ник></click> <#c0c0c0>- <#fcfcfc>заблокировать игрока",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner unban'><#745c97>/nightjoiner unban <ник></click> <#c0c0c0>- <#fcfcfc>разблокировать игрока",
                ""
            )
        )
        mainReloaded = checkValue(
            "nightjoiner.reloaded",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Плагин <#ace1af>успешно перезагружен</#ace1af>",
                ""
            )
        )
        mainTargetNotFound = checkValue(
            "nightjoiner.player-not-found",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Игрок <#745c97>%player%</#745c97> не найден",
                ""
            )
        )
        mainTargetBanned = checkValue(
            "nightjoiner.banned",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Игроку <#745c97>%player%</#745c97> <#d45079>заблокирована</#d45079> возможность установки сообщений",
                ""
            )
        )
        mainTargetUnbanned = checkValue(
            "nightjoiner.unbanned",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Игроку <#745c97>%player%</#745c97> <#ace1af>разблокирована</#ace1af> возможность установки сообщений",
                ""
            )
        )
        mainTargetReset = checkValue(
            "nightjoiner.reset",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Сообщения игрока <#745c97>%player%</#745c97> <#ace1af>сброшены</#ace1af>!",
                ""
            )
        )

        setJoinUsage = checkValue(
            "setjoin.usage", listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Использование <#745c97>/setjoin</#745c97>:",
                "  <#c0c0c0>‣ <click:suggest_command:'/setjoin'><#745c97>/setjoin <текст></click> <#c0c0c0>- <#fcfcfc>установить текст сообщения при входе",
                ""
            )
        )
        setJoinSuccess = checkValue(
            "setjoin.success",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы установили сообщение входа на <#ace1af>%message%</#ace1af>",
                ""
            )
        )
        setJoinBlocked = checkValue(
            "setjoin.blocked",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция установки сообщений при входе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        )

        setQuitUsage = checkValue(
            "setquit.usage", listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Использование <#745c97>/setjoin</#745c97>:",
                "  <#c0c0c0>‣ <click:suggest_command:'/setquit'><#745c97>/setquit <текст></click> <#c0c0c0>- <#fcfcfc>установить текст сообщения при выходе",
                ""
            )
        )
        setQuitSuccess = checkValue(
            "setquit.success",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы установили сообщение выхода на <#d45079>%message%</#d45079>",
                ""
            )
        )
        setQuitBlocked = checkValue(
            "setquit.blocked",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция установки сообщений при выходе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        )

        resetJoinSuccess = checkValue(
            "resetjoin.success",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы <#d45079>успешно сбросили</#d45079> сообщение при входе",
                ""
            )
        )
        resetJoinBlocked = checkValue(
            "resetjoin.blocked",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция сброса сообщений при входе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        )

        resetQuitSuccess = checkValue(
            "resetquit.success",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы <#d45079>успешно сбросили</#d45079> сообщение при выходе",
                ""
            )
        )
        resetQuitBlocked = checkValue(
            "resetquit.blocked",
            listOf(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция сброса сообщений при выходе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        )

        save()
    }

    private fun <T> checkValue(key: String, value: T): T {
        return if (!config.contains(key)) {
            config.set(key, value)
            value
        } else {
            config.get(key) as T
        }
    }
}
