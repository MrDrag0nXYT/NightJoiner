package zxc.mrdrag0nxyt.nightJoiner.config

import org.bukkit.configuration.file.YamlConfiguration
import zxc.mrdrag0nxyt.nightJoiner.NightJoiner
import java.io.File

abstract class AbstractConfig(
    protected val plugin: NightJoiner,
    private val fileName: String,
) {
    private var file = File(plugin.dataFolder, fileName)
    protected var config = init()

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
        updateConfig()
    }

    fun save() {
        try {
            config.save(file)
        } catch (e: Exception) {
            plugin.logger.severe(e.toString())
        }
    }

    abstract fun updateConfig()

    protected fun <T> checkValue(key: String, value: T): T {
        return if (!config.contains(key)) {
            config.set(key, value)
            value
        } else {
            config.get(key) as T
        }
    }
}