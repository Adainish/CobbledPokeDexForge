package io.github.adainish.cobbledpokedexforge;

import io.github.adainish.cobbledpokedexforge.config.ConfigurableDexConfig;
import io.github.adainish.cobbledpokedexforge.config.DexProgressionConfig;
import io.github.adainish.cobbledpokedexforge.config.RewardsConfig;
import io.github.adainish.cobbledpokedexforge.listener.PlayerListener;
import io.github.adainish.cobbledpokedexforge.obj.ConfigurableDex;
import io.github.adainish.cobbledpokedexforge.obj.DexProgression;
import io.github.adainish.cobbledpokedexforge.subscriptions.EventSubscriptions;
import io.github.adainish.cobbledpokedexforge.wrapper.Wrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
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
import java.util.Comparator;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CobbledPokeDexForge.MODID)
public class CobbledPokeDexForge {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobbledpokedexforge";


    public static final String MOD_NAME = "CobbledOutbreaks";
    public static final String VERSION = "1.0.0-Beta";
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

    // Directly reference a slf4j logger

    public static Wrapper wrapper;

    public static EventSubscriptions subscriptions;

    public static Logger getLog() {
        return log;
    }


    public CobbledPokeDexForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerShutDown(ServerStoppingEvent event)
    {
        log.warn("Saving player data...");
        wrapper.playerHashMap.forEach((uuid, player) -> {
            player.saveNoCache();
        });
        wrapper.playerHashMap.clear();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        log.info("Booting up %n by %authors %v %y"
                .replace("%n", MOD_NAME)
                .replace("%authors", AUTHORS)
                .replace("%v", VERSION)
                .replace("%y", YEAR)
        );
        initDirs();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event)
    {
        setServer(ServerLifecycleHooks.getCurrentServer());
        wrapper = new Wrapper();
     //load configurable dex from config
        initConfigs();
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
        configurableDex = configurableDexConfig.configurableDex;
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
