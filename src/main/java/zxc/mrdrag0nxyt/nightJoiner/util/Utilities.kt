package zxc.mrdrag0nxyt.nightJoiner.util

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player

fun setColorWithPlaceholders(player: Player?, from: String): Component {
    return MiniMessage.miniMessage().deserialize(
        MiniMessage.miniMessage().serialize(
            LegacyComponentSerializer.legacySection().deserialize(
                PlaceholderAPI.setPlaceholders(player, from).replace("&", "§")
            )
        )
            .replace("\\<", "<")
            .replace("\\>", ">")
    )
}

fun isVanished(player: Player): Boolean {
    for (metadataValue in player.getMetadata("vanished")) {
        if (metadataValue.asBoolean()) return true
    }
    return false
}
