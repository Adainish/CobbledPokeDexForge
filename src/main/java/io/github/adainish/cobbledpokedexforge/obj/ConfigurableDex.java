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

    public List<DexProgression> dexProgressionList = new ArrayList<>();

    public ConfigurableDex()
    {
        this.pokemonData = new HashMap<>();
        this.dexProgressionList = new ArrayList<>();
    }

    public void load()
    {
        for (Species sp: PokemonSpecies.INSTANCE.getSpecies()) {
            ConfigurableDexPokemon dexPokemon = new ConfigurableDexPokemon(sp.getNationalPokedexNumber());
            pokemonData.put(sp.getNationalPokedexNumber(), dexPokemon);
        }
        for (int i = 0; i < 5; i++) {
            DexProgression dexProgression = new DexProgression("example%i%".replace("%i%", String.valueOf(i + 1)));
            dexProgression.setPercentage(20 * (i + 1));
            dexProgressionList.add(dexProgression);
        }
        dexProgressionList.sort(Comparator.comparing(DexProgression::getPercentage).reversed());
    }

    public void syncConfigData(Player player)
    {
        //add new entries
        CobbledPokeDexForge.configurableDex.pokemonData.forEach((integer, configurableDexPokemon) -> {
            if (player.pokeDex.pokemonData.containsKey(integer))
                return;
            DexPokemon dexPokemon = new DexPokemon(integer);
            player.pokeDex.pokemonData.put(integer, dexPokemon);
        });
        pokemonData.forEach((integer, dexPokemon) -> {
            player.pokeDex.pokemonData.get(integer).rewardIDs = dexPokemon.rewardIDs;
        });
    }
}
