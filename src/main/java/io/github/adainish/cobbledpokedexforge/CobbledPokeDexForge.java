package io.github.adainish.cobbledpokedexforge;

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
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(CobbledPokeDexForge.MODID)
public class CobbledPokeDexForge {
    public static final String MODID = "cobbledpokedexforge";
    public static final String MOD_NAME = "CobbledPokeDexForge";
    public static final String VERSION = "1.0.2-Beta";
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

    public static EventSubscriptions subscriptions;

    public static Logger getLog() {
        return log;
    }

    public static PlayerStorage playerStorage;


    public CobbledPokeDexForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerShutDown(ServerStoppingEvent event)
    {
        this.handleShutDown();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        this.initDirs();
    }

    @SubscribeEvent
    public void onCommandRegistry(RegisterCommandsEvent event) {
        //register commands
        event.getDispatcher().register(Command.getCommand());
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event)
    {
        setServer(ServerLifecycleHooks.getCurrentServer());
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
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

    public void initDirs() {
        setConfigDir(new File(FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()) + "/CobbledPokeDex/"));
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
