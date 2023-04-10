package io.github.adainish.cobbledpokedexforge.subscriptions;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import io.github.adainish.cobbledpokedexforge.obj.Player;
import io.github.adainish.cobbledpokedexforge.storage.PlayerStorage;
import kotlin.Unit;

public class EventSubscriptions
{

    public EventSubscriptions()
    {
        subscribeToEvolving();
        subscribeToCapture();
        subscribeToFainting();
    }

    public void subscribeToFainting()
    {
        CobblemonEvents.POKEMON_FAINTED.subscribe(Priority.NORMAL, event -> {
            try {
                Player player = PlayerStorage.getPlayer(event.component1().getOwnerPlayer().getUUID());
                if (player != null)
                {
                    player.register(event.getPokemon(), true, false);
                    player.updateCache();
                }
            } catch (Exception e)
            {
                return Unit.INSTANCE;
            }

            return Unit.INSTANCE;
        });

    }

    public void subscribeToEvolving()
    {
        CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.NORMAL, event -> {
            try {
                Player player = PlayerStorage.getPlayer(event.component1().getOwnerPlayer().getUUID());
                if (player != null)
                {
                    player.register(event.getPokemon(), true, true);
                    player.updateCache();
                }
            } catch (Exception e)
            {
                return Unit.INSTANCE;
            }

            return Unit.INSTANCE;
        });

    }

    public void subscribeToCapture()
    {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, event -> {
            Player player = PlayerStorage.getPlayer(event.getPlayer().getUUID());
            if (player != null)
            {
                player.register(event.getPokemon(), true, true);
                player.updateCache();
            }
            return Unit.INSTANCE;
        });

    }
}
