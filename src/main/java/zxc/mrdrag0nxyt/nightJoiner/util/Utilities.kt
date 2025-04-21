package zxc.mrdrag0nxyt.nightJoiner.util

import org.bukkit.entity.Player

fun isVanished(player: Player): Boolean {
    for (metadataValue in player.getMetadata("vanished")) {
        if (metadataValue.asBoolean()) return true
    }
    return false
}
