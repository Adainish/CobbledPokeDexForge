package io.github.adainish.cobbledpokedexforge.obj;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ConfigurableDex
{
    public HashMap<Integer, ConfigurableDexPokemon> pokemonData = new HashMap<>();


    public ConfigurableDex()
    {
        this.pokemonData = new HashMap<>();
    }

    public void load()
    {
        for (Species sp: PokemonSpecies.INSTANCE.getSpecies()) {
            ConfigurableDexPokemon dexPokemon = new ConfigurableDexPokemon(sp.getNationalPokedexNumber());
            pokemonData.put(sp.getNationalPokedexNumber(), dexPokemon);
        }

    }

    public void syncConfigData(Player player)
    {
        //add new entries
        CobbledPokeDexForge.configurableDex.pokemonData.forEach((integer, configurableDexPokemon) -> {
            if (player.pokeDex.pokemonData.containsKey(integer))
                return;
            DexPokemon dexPokemon = new DexPokemon(integer);
            player.pokeDex.pokemonData.put(integer, dexPokemon);
            dexPokemon.rewardIDs = configurableDexPokemon.rewardIDs;
        });
        pokemonData.forEach((integer, dexPokemon) -> {
            player.pokeDex.pokemonData.get(integer).rewardIDs = dexPokemon.rewardIDs;
        });
    }
}
