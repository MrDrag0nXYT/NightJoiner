package zxc.MrDrag0nXYT.nightJoiner.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class Utilities {

    public static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component setColor(String from) {
        return miniMessage.deserialize(from);
    }

    public static Component setColorWithPlaceholders(Player player, String from) {
        return miniMessage.deserialize(
                miniMessage.serialize(
                                LegacyComponentSerializer.legacySection().deserialize(
                                        PlaceholderAPI.setPlaceholders(player, from).replace("&", "§")
                                )
                        )
                        .replace("\\<", "<")
                        .replace("\\>", ">")
        );
    }

    public static boolean isVanished(Player player) {
        for (MetadataValue metadataValue : player.getMetadata("vanished")) {
            if (metadataValue.asBoolean()) return true;
        }
        return false;
    }
}
