package zxc.MrDrag0nXYT.nightJoiner.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import zxc.MrDrag0nXYT.nightJoiner.NightJoiner;
import zxc.MrDrag0nXYT.nightJoiner.util.Utilities;
import zxc.MrDrag0nXYT.nightJoiner.config.Config;
import zxc.MrDrag0nXYT.nightJoiner.database.DatabaseManager;
import zxc.MrDrag0nXYT.nightJoiner.database.DatabaseWorker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerJoinQuitListener implements Listener {

    private final NightJoiner plugin;
    private Config config;
    private DatabaseManager databaseManager;

    public PlayerJoinQuitListener(NightJoiner plugin, Config config, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.config = config;
        this.databaseManager = databaseManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        YamlConfiguration yamlConfiguration = config.getConfig();

        if (yamlConfiguration.getBoolean("messages.motd.enabled", false)) {
            Player player = event.getPlayer();

            for (String string : yamlConfiguration.getStringList("messages.motd.text")) {
                player.sendMessage(
                        Utilities.setColorWithPlaceholders(player, string)
                );
            }
        }

        broadcast(event, yamlConfiguration, true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        YamlConfiguration yamlConfiguration = config.getConfig();

        broadcast(event, yamlConfiguration, false);
    }

    private void broadcast(PlayerEvent event, YamlConfiguration yamlConfiguration, boolean isJoin) {
        String isJoinPath = isJoin ? "join" : "quit";
        Player player = event.getPlayer();

        if (yamlConfiguration.getBoolean("vanish-check", false)) {
            if (Utilities.isVanished(player)) {
                return;
            }
        }

        if (player.hasPermission("nightjoiner.player.broadcast." + isJoinPath)) {

            DatabaseWorker databaseWorker = databaseManager.getDatabaseWorker();

            String eventMessage = null;

            try (Connection connection = databaseManager.getConnection()) {
                if (isJoin) {
                    eventMessage = databaseWorker.getJoinMessage(connection, player.getUniqueId(), player.getName());
                } else {
                    eventMessage = databaseWorker.getQuitMessage(connection, player.getUniqueId(), player.getName());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().severe(String.valueOf(e));
            }

            if (eventMessage == null) {
                eventMessage = yamlConfiguration.getString("messages.default." + isJoinPath);
            }

            List<Component> messages = new ArrayList<>();
            for (String string : yamlConfiguration.getStringList("messages." + isJoinPath)) {
                messages.add(Utilities.setColorWithPlaceholders(
                                player,
                                string.replace("%player_text%", eventMessage)
                        )
                );
            }

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                for (Component component : messages) {
                    onlinePlayer.sendMessage(component);
                }
            }

            if (yamlConfiguration.getBoolean("messages.show-in-console")) {
                for (Component component : messages) {
                    Bukkit.getConsoleSender().sendMessage(component);
                }
            }

        }
    }
}
