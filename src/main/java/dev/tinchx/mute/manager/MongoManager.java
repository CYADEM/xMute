package dev.tinchx.mute.manager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.tinchx.mute.MutePlugin;
import dev.tinchx.mute.utiliities.configuration.RootConfig;
import lombok.Getter;

public class MongoManager {

    @Getter
    private final MongoCollection profiles;

    public MongoManager() {
        RootConfig configuration = MutePlugin.getInstance().getSettings();
        MongoClient client;
        if (configuration.getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(configuration.getString("DATABASE.MONGO.HOST"),
                    configuration.getInt("DATABASE.MONGO.PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    configuration.getString("DATABASE.MONGO.AUTHENTICATION.USER"), configuration.getString("DATABASE.MONGO.AUTHENTICATION.DATABASE"),
                    configuration.getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray());

            client = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
        } else {
            client = new MongoClient(configuration.getString("DATABASE.MONGO.HOST"), configuration.getInt("DATABASE.MONGO.PORT"));
        }
        MongoDatabase database = client.getDatabase("xMute");
        this.profiles = database.getCollection("Profiles");
    }
}
