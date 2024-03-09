package io.github.adainish.cobbledpokedexforge;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import io.github.adainish.cobbledpokedexforge.cmd.Command;
import io.github.adainish.cobbledpokedexforge.config.ConfigurableDexConfig;
import io.github.adainish.cobbledpokedexforge.config.DBConfig;
import io.github.adainish.cobbledpokedexforge.config.DexProgressionConfig;
import io.github.adainish.cobbledpokedexforge.config.RewardsConfig;
import io.github.adainish.cobbledpokedexforge.listener.PlayerListener;
import io.github.adainish.cobbledpokedexforge.obj.ConfigurableDex;
import io.github.adainish.cobbledpokedexforge.storage.Database;
import io.github.adainish.cobbledpokedexforge.storage.PlayerStorage;
import io.github.adainish.cobbledpokedexforge.subscriptions.EventSubscriptions;
import io.github.adainish.cobbledpokedexforge.wrapper.Wrapper;
import kotlin.Unit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class CobbledPokeDexForge implements ModInitializer {
    public static final String MODID = "cobbledpokedexforge";
    public static final String MOD_NAME = "CobbledPokeDex";
    public static final String VERSION = "1.1.0";
    public static final String AUTHORS = "Winglet";
    public static final String YEAR = "2023";
    private static final Logger log = LogManager.getLogger(MOD_NAME);
    private static MinecraftServer server;
    private static File configDir;
    private static File storage;

    private static File playerStorageDir;

    public static ConfigurableDexConfig configurableDexConfig;

    public static ConfigurableDex configurableDex;

    public static RewardsConfig rewardsConfig;

    public static DexProgressionConfig dexProgressionConfig;
    public static DBConfig dbConfig;
    public static Wrapper wrapper;

    public EventSubscriptions subscriptions;

    public PlayerListener playerListener;

    public static Logger getLog() {
        return log;
    }

    public static PlayerStorage playerStorage;


    public CobbledPokeDexForge() {}

    @Override
    public void onInitialize() {
        this.commonSetup();
    }

    private void commonSetup() {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        this.initDirs();

        PlatformEvents.SERVER_STARTED.subscribe(Priority.NORMAL, t -> {
            setServer(t.getServer());
            playerStorage = new PlayerStorage();
            wrapper = new Wrapper();
            //load configurable dex from config
            this.initConfigs();
            if (dbConfig != null)
            {
                if (dbConfig.enabled)
                {
                    playerStorage.database = new Database();
                }
            }
            subscriptions = new EventSubscriptions();
            playerListener = new PlayerListener();

            return Unit.INSTANCE;
        });

        PlatformEvents.SERVER_STOPPING.subscribe(Priority.NORMAL, t -> {
            this.handleShutDown();
            return Unit.INSTANCE;
        });


        CommandRegistrationCallback.EVENT.register((dispatcher, registryaccess, environment) -> {
            dispatcher.register(Command.getCommand());
        });
    }


    public void initDirs() {
        setConfigDir(new File(FabricLoader.getInstance().getConfigDir() + "/CobbledPokeDex/"));
        getConfigDir().mkdir();
        setStorage(new File(getConfigDir(), "/storage/"));
        getStorage().mkdirs();
        setPlayerStorageDir(new File(storage, "/playerdata/"));
        getPlayerStorageDir().mkdirs();
    }



    public void initConfigs() {
        RewardsConfig.writeConfig();
        rewardsConfig = RewardsConfig.getConfig();

        DexProgressionConfig.writeConfig();
        dexProgressionConfig = DexProgressionConfig.getConfig();


        ConfigurableDexConfig.writeConfig();
        configurableDexConfig = ConfigurableDexConfig.getConfig();

        if (configurableDexConfig != null) {
            configurableDex = configurableDexConfig.configurableDex;
        }

        DBConfig.writeConfig();
        dbConfig = DBConfig.getConfig();
    }

    public void handleShutDown()
    {
        playerStorage.saveAll();
        if (playerStorage.database != null)
            playerStorage.database.shutdown();
    }


    public static MinecraftServer getServer() {
        return server;
    }

    public static void setServer(MinecraftServer server) {
        CobbledPokeDexForge.server = server;
    }

    public static File getConfigDir() {
        return configDir;
    }

    public static void setConfigDir(File configDir) {
        CobbledPokeDexForge.configDir = configDir;
    }

    public static File getStorage() {
        return storage;
    }

    public static void setStorage(File storage) {
        CobbledPokeDexForge.storage = storage;
    }

    public static File getPlayerStorageDir() {
        return playerStorageDir;
    }

    public static void setPlayerStorageDir(File playerStorageDir) {
        CobbledPokeDexForge.playerStorageDir = playerStorageDir;
    }




}
