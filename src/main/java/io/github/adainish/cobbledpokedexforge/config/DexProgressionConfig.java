package io.github.adainish.cobbledpokedexforge.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.obj.ConfigurableDexProgression;
import io.github.adainish.cobbledpokedexforge.util.Adapters;

import java.io.*;
import java.util.HashMap;
public class DexProgressionConfig
{
    public HashMap<String, ConfigurableDexProgression> configurableDexProgressions = new HashMap<>();

    public DexProgressionConfig()
    {
        this.configurableDexProgressions = new HashMap<>();
        load();
    }

    public static void writeConfig()
    {
        File dir = CobbledPokeDexForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        DexProgressionConfig config = new DexProgressionConfig();
        try {
            File file = new File(dir, "dexprogression.json");
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

    public static DexProgressionConfig getConfig()
    {
        File dir = CobbledPokeDexForge.getConfigDir();
        dir.mkdirs();
        Gson gson  = Adapters.PRETTY_MAIN_GSON;
        File file = new File(dir, "dexprogression.json");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            CobbledPokeDexForge.getLog().error("Something went wrong attempting to read the Config");
            return null;
        }

        return gson.fromJson(reader, DexProgressionConfig.class);
    }

    public void load()
    {
        for (int i = 0; i < 5; i++) {
            ConfigurableDexProgression dexProgression = new ConfigurableDexProgression("example%i%".replace("%i%", String.valueOf(i + 1)));
            dexProgression.setPercentage(20 * (i + 1));
            dexProgression.setGuiSlot(9 + (2 * i));
            configurableDexProgressions.put(dexProgression.getId(), dexProgression);
        }
    }
}
