package zxc.MrDrag0nXYT.nightJoiner.util.config;

import org.bukkit.configuration.file.YamlConfiguration;
import zxc.MrDrag0nXYT.nightJoiner.NightJoiner;

import java.io.File;
import java.util.List;

public class Config {

    private final NightJoiner plugin;

    private final String fileName;
    private File file;
    private YamlConfiguration config;

    public Config(NightJoiner plugin) {
        this.plugin = plugin;
        this.fileName = "config.yml";

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
        checkConfigValue("database.type", "SQLITE");

        checkConfigValue("messages.show-in-console", true);
        checkConfigValue("messages.join", List.of("", " <#ace1af>+</#ace1af> &#fcfcfc%luckperms_prefix% %player_name% &#fcfcfc%player_text%", ""));
        checkConfigValue("messages.quit", List.of("", " <#d45079>-</#d45079> &#fcfcfc%luckperms_prefix% %player_name% &#fcfcfc%player_text%", ""));

        checkConfigValue("messages.default.join", "joined game");
        checkConfigValue("messages.default.quit", "leaved");

        checkConfigValue("messages.motd.enabled", false);
        checkConfigValue("messages.motd.text", List.of("", " <#fcfcfc>Welcome, <#745c97>%player_name%</#745c97>!", " <#c0c0c0>‣ <#fcfcfc>Your group: %luckperms_prefix%", ""));

        save();
    }

}
