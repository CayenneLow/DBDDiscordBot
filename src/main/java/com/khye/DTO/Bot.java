package com.khye.DTO;

import java.util.UUID;

public class Bot {
    private UUID uuid;
    private long time_created;    

    public Bot(UUID uuid, long time_created) {
        this.uuid = uuid;
        this.time_created = time_created;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getTime_created() {
        return this.time_created;
    }

    public void setTime_created(long time_created) {
        this.time_created = time_created;
    }

    @Override
    public String toString() {
        return "{" +
            " uuid='" + getUuid() + "'" +
            ", time_created='" + getTime_created() + "'" +
            "}";
    }
    
}