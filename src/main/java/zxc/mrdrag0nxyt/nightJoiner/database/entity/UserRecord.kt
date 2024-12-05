package zxc.mrdrag0nxyt.nightJoiner.database.entity

import java.util.*

class UserRecord(
    id: Long,
    username: String?,
    uuid: UUID,
    join_message: String?,
    quit_message: String?,
    is_blocked: Int
) {
    constructor(username: String?, uuid: UUID) : this(-1, username, uuid, null, null, 0)

    val id: Long
    val username: String?
    val uuid: UUID
    val join_message: String?
    val quit_message: String?
    val is_blocked: Int

    init {
        var is_blocked = is_blocked
        if (is_blocked != 1) is_blocked = 0

        this.id = id
        this.username = username
        this.uuid = uuid
        this.join_message = join_message
        this.quit_message = quit_message
        this.is_blocked = is_blocked
    }
}
