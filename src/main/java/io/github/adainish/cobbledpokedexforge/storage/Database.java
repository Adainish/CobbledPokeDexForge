package io.github.adainish.cobbledpokedexforge.storage;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.config.MongoCodecStringArray;
import io.github.adainish.cobbledpokedexforge.obj.Player;
import io.github.adainish.cobbledpokedexforge.obj.PokeDex;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.UUID;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database
{
    public MongoClientSettings mongoClientSettings;
    public MongoClient mongoClient;
    public MongoDatabase database;
    public MongoCollection<Document> collection;

    public Database()
    {
        if (CobbledPokeDexForge.dbConfig.enabled)
        {
            if (this.init())
            {
                CobbledPokeDexForge.getLog().warn("Successfully initialised database connection");
            } else CobbledPokeDexForge.getLog().warn("Failed connecting to the database- please check the error for more info or contact the developer");
        } else CobbledPokeDexForge.getLog().warn("Database not enabled, now using local storage files.");
    }

    public void shutdown()
    {
        //close connection
        if (mongoClient != null) {

            CobbledPokeDexForge.getLog().warn("Shutting down database connection");
            mongoClient.close();
        } else CobbledPokeDexForge.getLog().warn("Something went wrong while shutting down the mongo db, was it ever set up to begin with?");
    }

    // Static method to create a Player object from a Document
    public Player fromDocument(Document document) {
        Gson gson = new Gson();
        return gson.fromJson(document.toJson(), Player.class);
    }

    public boolean save(Player player)
    {
        try {
            Document playerDoc = player.toDocument();
            // Insert or replace the document in MongoDB using the player's UUID as the key
            collection.replaceOne(Filters.eq("uuid", player.uuid.toString()), playerDoc, new ReplaceOptions().upsert(true));
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        CobbledPokeDexForge.getLog().error("Something went wrong while saving a player to the database... refer to error above");
        return false;
    }

    public Player getPlayer(UUID uuid)
    {
        try {
            // Query MongoDB to find the player Document by UUID
            Document playerDocument = collection.find(Filters.eq("uuid", uuid.toString())).first();

            if (playerDocument != null) {
                // Convert the Document back to a Player object
                return this.fromDocument(playerDocument);
            } else {
                return null; // Player not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean makePlayer(UUID uuid)
    {
        try {
            // Check if a player with the same UUID already exists in the database
            if (collection.find(Filters.eq("uuid", uuid.toString())).first() != null) {
                // A player with the same UUID already exists, so return false to indicate failure
                return false;
            }
            // Create a new Player object
            Player player = CobbledPokeDexForge.playerStorage.getPlayer(uuid);
            if (player == null)
                player = new Player(uuid);
                // Convert Player object to Document
                Document playerDocument = player.toDocument();

                // Insert the new player Document into MongoDB
                collection.insertOne(playerDocument);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean makePlayer(Player player)
    {
        try {
            // Check if a player with the same UUID already exists in the database
            if (collection.find(Filters.eq("uuid", player.uuid.toString())).first() != null) {
                // A player with the same UUID already exists, so return false to indicate failure
                return false;
            }
            // Convert Player object to Document
            Document playerDocument = player.toDocument();

            // Insert the new player Document into MongoDB
            collection.insertOne(playerDocument);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean init()
    {
        try {
            ConnectionString connectionString = new ConnectionString(CobbledPokeDexForge.dbConfig.mongoDBURI);
            CodecRegistry codecRegistry = fromRegistries(
                    CodecRegistries.fromCodecs(new MongoCodecStringArray()), // <---- this is the custom codec
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );
            mongoClientSettings = MongoClientSettings.builder().codecRegistry(codecRegistry).applyConnectionString(connectionString).retryWrites(true).build();
            mongoClient = MongoClients.create(mongoClientSettings);
            database = mongoClient.getDatabase(CobbledPokeDexForge.dbConfig.database);
            collection = database.getCollection(CobbledPokeDexForge.dbConfig.tableName);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return false;
    }

    public boolean migratePlayerData()
    {
        CobbledPokeDexForge.playerStorage.getAllPlayersFromFiles().forEach(player -> {
            CobbledPokeDexForge.getLog().warn("Now migrating data for: %uuid%".replace("%uuid%", player.uuid.toString()));
            if (makePlayer(player.uuid)) {
                CobbledPokeDexForge.getLog().warn("Migrated data for: %uuid%".replace("%uuid%", player.uuid.toString()));
            } else {
                CobbledPokeDexForge.getLog().warn("Couldn't make a player entry for %uuid%, did this player already exist in the database?".replace("%uuid%", player.uuid.toString()));
            }
        });

        return true;
    }

}
