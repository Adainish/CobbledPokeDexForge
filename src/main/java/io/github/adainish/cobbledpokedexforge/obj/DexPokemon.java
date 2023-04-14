package io.github.adainish.cobbledpokedexforge.obj;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

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

}
