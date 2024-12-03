package zxc.MrDrag0nXYT.nightJoiner.database.entity;

import java.util.UUID;

public record UserRecord(long id, String username, UUID uuid, String join_message, String quit_message, int is_blocked) {

    public UserRecord(String username, UUID uuid) {
        this(-1, username, uuid, null, null, 0);
    }

    public UserRecord(long id, String username, UUID uuid, String join_message, String quit_message, int is_blocked) {
        if (is_blocked != 1) is_blocked = 0;

        this.id = id;
        this.username = username;
        this.uuid = uuid;
        this.join_message = join_message;
        this.quit_message = quit_message;
        this.is_blocked = is_blocked;
    }

}
