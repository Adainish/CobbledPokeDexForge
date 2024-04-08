package io.github.adainish.cobbledpokedexforge.listener;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.obj.Player;
import kotlin.Unit;
import net.minecraft.server.level.ServerPlayer;


public class PlayerListener
{

    public PlayerListener()
    {

        PlatformEvents.SERVER_PLAYER_LOGIN.subscribe(Priority.NORMAL, event -> {
            Player player = CobbledPokeDexForge.playerStorage.getPlayer(event.getPlayer().getUUID());
            if (player == null) {
                CobbledPokeDexForge.playerStorage.makePlayer(event.getPlayer());
                player = CobbledPokeDexForge.playerStorage.getPlayer(event.getPlayer().getUUID());
            }

            if (player != null) {
                player.syncWithConfigurable();
                player.registerFromStorage();
                player.updateCache();
            }
            return Unit.INSTANCE;
        });

        PlatformEvents.SERVER_PLAYER_LOGOUT.subscribe(Priority.NORMAL, event -> {
            ServerPlayer player = event.getPlayer();
            if (player != null) {
                Player p = CobbledPokeDexForge.playerStorage.getPlayer(player.getUUID());
                if (p != null) {
                    p.save();
                }
            }
            return Unit.INSTANCE;
        });
    }
}
