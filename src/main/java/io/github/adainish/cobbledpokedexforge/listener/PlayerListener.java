package io.github.adainish.cobbledpokedexforge.listener;

import io.github.adainish.cobbledpokedexforge.obj.Player;
import io.github.adainish.cobbledpokedexforge.storage.PlayerStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerListener
{
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() == null) {
            return;
        }

        Player player = PlayerStorage.getPlayer(event.getEntity().getUUID());
        if (player == null) {
            PlayerStorage.makeWonderTradePlayer((ServerPlayer) event.getEntity());
            player = PlayerStorage.getPlayer(event.getEntity().getUUID());
        }

        if (player != null) {
            player.syncWithConfigurable();
            player.registerFromStorage();
            player.updateCache();
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() != null) {
            Player player = PlayerStorage.getPlayer(event.getEntity().getUUID());
            if (player != null) {
                PlayerStorage.savePlayer(player);
            }
        }
    }
}
