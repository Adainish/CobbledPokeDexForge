package io.github.adainish.cobbledpokedexforge.obj;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DexPokemon
{
    public int pokeDexNumber = 1;
    public boolean registered = false;
    public boolean seen = false;
    public List<String> rewardIDs = new ArrayList<>();
    public boolean claimed = false;

    public DexPokemon(int pokeDexNumber)
    {
        this.pokeDexNumber = pokeDexNumber;
    }

    public Pokemon getPokemon()
    {
        return PokemonSpecies.INSTANCE.getByPokedexNumber(pokeDexNumber, "cobblemon").create(100);
    }

    public String registeredStatus()
    {
        String s = "&cNot Registered";
        if (registered)
            s = "&aRegistered";
        return s;
    }

    public String seenStatus()
    {
        String s = "&cUnseen";
        if (seen)
            s = "&aSeen";
        return s;
    }

    public List<Reward> getRewards()
    {
        return rewardIDs.stream().filter(s -> CobbledPokeDexForge.rewardsConfig.rewardHashMap.containsKey(s)).map(s -> CobbledPokeDexForge.rewardsConfig.rewardHashMap.get(s)).collect(Collectors.toList());
    }

    public void claimRewards(ServerPlayer player)
    {
        claimed = true;
        for (Reward r:getRewards()) {
            r.giveRewards(player);
        }
    }

}
