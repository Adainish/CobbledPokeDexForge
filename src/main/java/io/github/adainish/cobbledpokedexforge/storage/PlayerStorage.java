package io.github.adainish.cobbledpokedexforge.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.obj.Player;
import io.github.adainish.cobbledpokedexforge.util.Adapters;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.util.UUID;

public class PlayerStorage
{
    public static void makeWonderTradePlayer(ServerPlayer player) {
        File dir = CobbledPokeDexForge.getPlayerStorageDir();
        dir.mkdirs();


        Player gormottiPlayer = new Player(player.getUUID());

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.getUUID())));
        if (file.exists()) {
            CobbledPokeDexForge.getLog().error("There was an issue generating the Player, Player already exists? Ending function");
            return;
        }

        Gson gson = Adapters.PRETTY_MAIN_GSON;
        String json = gson.toJson(gormottiPlayer);

        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerNoCache(Player player) {

        File dir = CobbledPokeDexForge.getPlayerStorageDir();
        dir.mkdirs();

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            CobbledPokeDexForge.getLog().error("Something went wrong attempting to read the Player Data");
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePlayer(Player player) {

        File dir = CobbledPokeDexForge.getPlayerStorageDir();
        dir.mkdirs();

        File file = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(player.uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (reader == null) {
            CobbledPokeDexForge.getLog().error("Something went wrong attempting to read the Player Data");
            return;
        }

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.updateCache();
    }

    public static Player getPlayer(UUID uuid) {
        File dir = CobbledPokeDexForge.getPlayerStorageDir();
        dir.mkdirs();

        if (CobbledPokeDexForge.wrapper.playerHashMap.containsKey(uuid))
            return CobbledPokeDexForge.wrapper.playerHashMap.get(uuid);

        File dataFile = new File(dir, "%uuid%.json".replaceAll("%uuid%", String.valueOf(uuid)));
        Gson gson = Adapters.PRETTY_MAIN_GSON;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(dataFile));
        } catch (FileNotFoundException e) {
            CobbledPokeDexForge.getLog().error("Something went wrong attempting to read the Player Data, new Player Perhaps?");
            return null;
        }

        return gson.fromJson(reader, Player.class);
    }
}
