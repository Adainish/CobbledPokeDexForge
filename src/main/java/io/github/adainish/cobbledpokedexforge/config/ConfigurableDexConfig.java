package io.github.adainish.cobbledpokedexforge.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.obj.ConfigurableDex;
import io.github.adainish.cobbledpokedexforge.util.Adapters;

import java.io.*;

public class ConfigurableDexConfig
{
    public ConfigurableDex configurableDex;
    public ConfigurableDexConfig()
    {
        this.configurableDex = new ConfigurableDex();
        this.configurableDex.load();
    }


    public static void writeConfig()
    {
        File dir = CobbledPokeDexForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        ConfigurableDexConfig config = new ConfigurableDexConfig();
        try {
            File file = new File(dir, "config.json");
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

    public static ConfigurableDexConfig getConfig()
    {
        File dir = CobbledPokeDexForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "config.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobbledPokeDexForge.getLog().error("Something went wrong attempting to read the Config");
            return null;
        }

        return gson.fromJson(reader, ConfigurableDexConfig.class);
    }

}
