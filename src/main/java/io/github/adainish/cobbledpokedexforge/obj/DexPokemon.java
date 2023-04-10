package io.github.adainish.cobbledpokedexforge.obj;

import com.cobblemon.mod.common.pokemon.Species;

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


}
