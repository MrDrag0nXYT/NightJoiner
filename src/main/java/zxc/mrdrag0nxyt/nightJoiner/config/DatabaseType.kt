package zxc.mrdrag0nxyt.nightJoiner.config

enum class DatabaseType(val type: String) {
    SQLITE("sqlite"),
    MYSQL("mysql");

    companion object {
        fun fromStringType(type: String): DatabaseType {
            return entries.find { it.type.equals(type, ignoreCase = true) } ?: SQLITE
        }
    }
}
