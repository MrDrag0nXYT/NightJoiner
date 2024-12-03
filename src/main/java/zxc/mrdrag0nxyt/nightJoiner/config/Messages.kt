package zxc.MrDrag0nXYT.nightJoiner.config;

import org.bukkit.configuration.file.YamlConfiguration;
import zxc.MrDrag0nXYT.nightJoiner.NightJoiner;

import java.io.File;
import java.util.List;

public class Messages {

    private final NightJoiner plugin;

    private final String fileName;
    private File file;
    private YamlConfiguration config;

    public Messages(NightJoiner plugin) {
        this.plugin = plugin;
        this.fileName = "messages.yml";

        init();
        updateConfig();
    }

    private void init() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        try {
            config.load(file);
        } catch (Exception e) {
            plugin.getLogger().severe(String.valueOf(e));
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            plugin.getLogger().severe(String.valueOf(e));
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }



    /*
     * Checking config values
     */

    private void checkConfigValue(String key, Object defaultValue) {
        if (!config.contains(key)) {
            config.set(key, defaultValue);
        }
    }

    private void updateConfig() {
        checkConfigValue("global.no-permission", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>У вас <#d45079>недостаточно прав</#d45079> для выполнения этого действия", ""));
        checkConfigValue("global.not-player", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Эта команда <#d45079>недоступна</#d45079> для выполнения из консоли", ""));
        checkConfigValue("global.database-error", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>При выполнении действия <#d45079>произошла ошибка</#d45079> в базе данных", ""));

        checkConfigValue("nightjoiner.usage", List.of(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Информация",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner reload'><#745c97>/nightjoiner reload</click> <#c0c0c0>- <#fcfcfc>перезагрузить плагин",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner ban'><#745c97>/nightjoiner ban <ник></click> <#c0c0c0>- <#fcfcfc>заблокировать игрока",
                "  <#c0c0c0>‣ <click:suggest_command:'/nightjoiner unban'><#745c97>/nightjoiner unban <ник></click> <#c0c0c0>- <#fcfcfc>разблокировать игрока",
                ""
        ));
        checkConfigValue("nightjoiner.reloaded", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Плагин <#ace1af>успешно перезагружен</#ace1af>", ""));
        checkConfigValue("nightjoiner.player-not-found", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Игрок <#745c97>%player%</#745c97> не найден", ""));
        checkConfigValue("nightjoiner.banned", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Игроку <#745c97>%player%</#745c97> <#d45079>заблокирована</#d45079> возможность установки сообщений", ""));
        checkConfigValue("nightjoiner.unbanned", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Игроку <#745c97>%player%</#745c97> <#ace1af>разблокирована</#ace1af> возможность установки сообщений", ""));

        checkConfigValue("setjoin.usage", List.of(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Использование <#745c97>/setjoin</#745c97>:",
                "  <#c0c0c0>‣ <click:suggest_command:'/setjoin'><#745c97>/setjoin <текст></click> <#c0c0c0>- <#fcfcfc>установить текст сообщения при входе",
                ""
        ));
        checkConfigValue("setjoin.success", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы установили сообщение входа на <#ace1af>%message%</#ace1af>", ""));
        checkConfigValue("setjoin.blocked", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция установки сообщений при входе была <#d45079>заблокирована за нарушение правил</#d45079>!", ""));

        checkConfigValue("setquit.usage", List.of(
                "",
                " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Использование <#745c97>/setjoin</#745c97>:",
                "  <#c0c0c0>‣ <click:suggest_command:'/setquit'><#745c97>/setquit <текст></click> <#c0c0c0>- <#fcfcfc>установить текст сообщения при выходе",
                ""
        ));
        checkConfigValue("setquit.success", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы установили сообщение выхода на <#d45079>%message%</#d45079>", ""));
        checkConfigValue("setquit.blocked", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция установки сообщений при выходе была <#d45079>заблокирована за нарушение правил</#d45079>!", ""));

        checkConfigValue("resetjoin.success", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы <#d45079>успешно сбросили</#d45079> сообщение при входе", ""));
        checkConfigValue("resetjoin.blocked", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция сброса сообщений при входе была <#d45079>заблокирована за нарушение правил</#d45079>!", ""));

        checkConfigValue("resetquit.success", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Вы <#d45079>успешно сбросили</#d45079> сообщение при выходе", ""));
        checkConfigValue("resetquit.blocked", List.of("", " <#745c97>NightCodes <#c0c0c0>› <#fcfcfc>Функция сброса сообщений при выходе была <#d45079>заблокирована за нарушение правил</#d45079>!", ""));

        save();
    }

}
