package zxc.mrdrag0nxyt.nightJoiner.config

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner

class Messages(plugin: NightJoiner) : AbstractConfig(plugin, "messages.yml") {
    var globalNoPermission = listOf(Component.empty().asComponent())
        private set
    var globalNotPlayer = listOf(Component.empty().asComponent())
        private set
    var globalDatabaseError = listOf(Component.empty().asComponent())
        private set

    var mainUsage = listOf(Component.empty().asComponent())
        private set
    var mainReloaded = listOf(Component.empty().asComponent())
        private set
    var mainTargetNotFound = listOf(Component.empty().asComponent())
        private set
    var mainTargetBanned = listOf(Component.empty().asComponent())
        private set
    var mainTargetUnbanned = listOf(Component.empty().asComponent())
        private set
    var mainTargetReset = listOf(Component.empty().asComponent())
        private set

    var setJoinUsage = listOf(Component.empty().asComponent())
        private set
    var setJoinSuccess = listOf(Component.empty().asComponent())
        private set
    var setJoinBlocked = listOf(Component.empty().asComponent())
        private set

    var setQuitUsage = listOf(Component.empty().asComponent())
        private set
    var setQuitSuccess = listOf(Component.empty().asComponent())
        private set
    var setQuitBlocked = listOf(Component.empty().asComponent())
        private set

    var resetJoinSuccess = listOf(Component.empty().asComponent())
        private set
    var resetJoinBlocked = listOf(Component.empty().asComponent())
        private set

    var resetQuitSuccess = listOf(Component.empty().asComponent())
        private set
    var resetQuitBlocked = listOf(Component.empty().asComponent())
        private set

    init {
        updateConfig()
    }

    /*
    * Checking config values
    */

    override fun updateConfig() {
        val miniMessage = MiniMessage.miniMessage()

        globalNoPermission = checkValue(
            "global.no-permission",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>У вас <#d45079>недостаточно прав</#d45079> для выполнения этого действия",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        globalNotPlayer = checkValue(
            "global.not-player",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Эта команда <#d45079>недоступна</#d45079> для выполнения из консоли",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        globalDatabaseError = checkValue(
            "global.database-error",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>При выполнении действия <#d45079>произошла ошибка</#d45079> в базе данных",
                ""
            )
        ).map { miniMessage.deserialize(it) }

        mainUsage = checkValue(
            "nightjoiner.usage", listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Информация",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner reload'><#745c97>/nightjoiner reload</click> <#c0c0c0>- <#fcfcfc>перезагрузить плагин",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner ban'><#745c97>/nightjoiner ban <ник></click> <#c0c0c0>- <#fcfcfc>заблокировать игрока",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner unban'><#745c97>/nightjoiner unban <ник></click> <#c0c0c0>- <#fcfcfc>разблокировать игрока",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        mainReloaded = checkValue(
            "nightjoiner.reloaded",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Плагин <#ace1af>успешно перезагружен</#ace1af>",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        mainTargetNotFound = checkValue(
            "nightjoiner.player-not-found",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Игрок <#745c97>%player%</#745c97> не найден",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        mainTargetBanned = checkValue(
            "nightjoiner.banned",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Игроку <#745c97>%player%</#745c97> <#d45079>заблокирована</#d45079> возможность установки сообщений",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        mainTargetUnbanned = checkValue(
            "nightjoiner.unbanned",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Игроку <#745c97>%player%</#745c97> <#ace1af>разблокирована</#ace1af> возможность установки сообщений",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        mainTargetReset = checkValue(
            "nightjoiner.reset",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Сообщения игрока <#745c97>%player%</#745c97> <#ace1af>сброшены</#ace1af>!",
                ""
            )
        ).map { miniMessage.deserialize(it) }

        setJoinUsage = checkValue(
            "setjoin.usage", listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Использование <#745c97>/setjoin</#745c97>:",
                "  <#c0c0c0>‣ <click:suggest_command:'/setjoin'><#745c97>/setjoin <текст></click> <#c0c0c0>- <#fcfcfc>установить текст сообщения при входе",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        setJoinSuccess = checkValue(
            "setjoin.success",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Вы установили сообщение входа на <#ace1af>%message%</#ace1af>",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        setJoinBlocked = checkValue(
            "setjoin.blocked",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Функция установки сообщений при входе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        ).map { miniMessage.deserialize(it) }

        setQuitUsage = checkValue(
            "setquit.usage", listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Использование <#745c97>/setjoin</#745c97>:",
                "  <#c0c0c0>‣ <click:suggest_command:'/setquit'><#745c97>/setquit <текст></click> <#c0c0c0>- <#fcfcfc>установить текст сообщения при выходе",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        setQuitSuccess = checkValue(
            "setquit.success",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Вы установили сообщение выхода на <#d45079>%message%</#d45079>",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        setQuitBlocked = checkValue(
            "setquit.blocked",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Функция установки сообщений при выходе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        ).map { miniMessage.deserialize(it) }

        resetJoinSuccess = checkValue(
            "resetjoin.success",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Вы <#d45079>успешно сбросили</#d45079> сообщение при входе",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        resetJoinBlocked = checkValue(
            "resetjoin.blocked",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Функция сброса сообщений при входе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        ).map { miniMessage.deserialize(it) }

        resetQuitSuccess = checkValue(
            "resetquit.success",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Вы <#d45079>успешно сбросили</#d45079> сообщение при выходе",
                ""
            )
        ).map { miniMessage.deserialize(it) }
        resetQuitBlocked = checkValue(
            "resetquit.blocked",
            listOf(
                "",
                " <#745c97>NightJoiner <#c0c0c0>› <#fcfcfc>Функция сброса сообщений при выходе была <#d45079>заблокирована за нарушение правил</#d45079>!",
                ""
            )
        ).map { miniMessage.deserialize(it) }

        save()
    }
}
