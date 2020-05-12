package dev.tinchx.mute.profile;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.tinchx.mute.MutePlugin;
import dev.tinchx.mute.utiliities.document.DocumentSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;

public class Profile implements DocumentSerializer {

    @Getter
    private final UUID uuid;
    private long muteTime;
    @Setter
    private boolean permanentMute;

    public Profile(UUID uuid) {
        this.uuid = uuid;

        load();
    }

    public long getMuteTime() {
        if (permanentMute) {
            return muteTime;
        }
        return muteTime - System.currentTimeMillis();
    }

    public void setMuteTime(long muteTime) {
        this.muteTime = muteTime + System.currentTimeMillis();
    }

    private void load() {
        Document document = (Document) MutePlugin.getInstance().getMongoManager().getProfiles().find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (document.containsKey("playerMute")) {
                muteTime = document.getLong("playerMute");
            }
        }
    }

    public void save() {
        MutePlugin.getInstance().getMongoManager().getProfiles().replaceOne(Filters.eq("uuid", uuid.toString()), serialize(), new UpdateOptions().upsert(true));
    }

    @Override
    public Document serialize() {
        Document document = new Document();
        document.put("uuid", uuid.toString());
        if (getMuteTime() > 0L) {
            document.put("playerMute", getMuteTime());
        }
        return document;
    }
}
