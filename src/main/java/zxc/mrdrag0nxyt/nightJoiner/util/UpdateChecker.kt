package zxc.mrdrag0nxyt.nightJoiner.util

import com.google.gson.GsonBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import zxc.mrdrag0nxyt.nightJoiner.config.Config
import java.net.HttpURLConnection
import java.net.URL

class UpdateChecker(
    private val plugin: NightJoiner,
    private val config: Config
) : Listener {

    private val REPOSITORY = "MrDrag0nXYT/NightJoiner"
    private val GITHUB_RELEASES_LINK = "https://api.github.com/repos/${REPOSITORY}/releases/latest"

    private var isUpdateFound = false
    private var currentVersion = plugin.description.version
    private lateinit var releaseEntity: ReleaseEntity

    private var message: List<Component> = listOf()

    init {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val url = URL(GITHUB_RELEASES_LINK)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.doInput = true
            connection.doOutput = false

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val gson = GsonBuilder().setPrettyPrinting().create()
                releaseEntity = gson.fromJson(response, ReleaseEntity::class.java)

                if (releaseEntity.tag_name != currentVersion) {
                    isUpdateFound = true

                    val miniMessage = MiniMessage.miniMessage()
                    message = listOf(
                        miniMessage.deserialize(" "),
                        miniMessage.deserialize(" <#a880ff>NightJoiner <#696969>> <#fffafa>There are found update <#a880ff>${releaseEntity.name}"),
                        miniMessage.deserialize(" <#fffafa>Your version - <#dc143c>${currentVersion}</#dc143c>, available <#00ff7f>${releaseEntity.tag_name}</#00ff7f>"),
                        miniMessage.deserialize(" "),
                        miniMessage.deserialize(" <#fffafa>You can download it here - <#a880ff><click:open_url:'${releaseEntity.html_url}'>${releaseEntity.html_url}</click>"),
                        miniMessage.deserialize(" ")
                    )
                    announceUpdate()
                }
            }
        })
    }

    @EventHandler
    fun announceUpdate(event: PlayerJoinEvent) {
        if (!isUpdateFound) return
        if (!config.announceUpdateMessages) return
        if (!event.player.hasPermission("nightjoiner.admin.checkupdates")) return

        announceUpdate(event.player)
    }

    private fun announceUpdate(sender: CommandSender = plugin.server.consoleSender) {
        for (component in message) {
            sender.sendMessage(component)
        }
    }

}

data class ReleaseEntity(
    val tag_name: String,
    val name: String,
    val html_url: String
)
