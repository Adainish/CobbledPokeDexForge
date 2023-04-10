package io.github.adainish.cobbledpokedexforge.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.obj.Reward;
import io.github.adainish.cobbledpokedexforge.util.Adapters;

import java.io.*;
import java.util.HashMap;

public class RewardsConfig
{

    public HashMap<String, Reward> rewardHashMap = new HashMap<>();

    public RewardsConfig()
    {
        this.rewardHashMap = new HashMap<>();
        loadDefaultRewards();
    }

    public static void writeConfig()
    {
        File dir = CobbledPokeDexForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        RewardsConfig config = new RewardsConfig();
        try {
            File file = new File(dir, "rewards.json");
            if (file.exists())
                return;
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            String json = gson.toJson(config);
            writer.write(json);
            writer.close();
        } catch (IOException e)
        {
            CobbledPokeDexForge.getLog().warn(e);
        }
    }

    public static RewardsConfig getConfig()
    {
        File dir = CobbledPokeDexForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "rewards.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobbledPokeDexForge.getLog().error("Something went wrong attempting to read the Config");
            return null;
        }

        return gson.fromJson(reader, RewardsConfig.class);
    }

    public void loadDefaultRewards()
    {
        if (!rewardHashMap.isEmpty())
            return;

        for (int i = 0; i < 3; i++) {
            Reward reward = new Reward("examplereward%i%".replace("%i%", String.valueOf(i + 1)));
            reward.commands.add("broadcast I am a default reward");
            rewardHashMap.put(reward.identifier, reward);
        }
    }
}
