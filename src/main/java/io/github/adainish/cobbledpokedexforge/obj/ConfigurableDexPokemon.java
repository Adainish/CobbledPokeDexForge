package io.github.adainish.cobbledpokedexforge.obj;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableDexPokemon
{
    public int pokeDexNumber = 1;
    public List<String> rewardIDs = new ArrayList<>();

    public ConfigurableDexPokemon(int pokeDexNumber)
    {
        this.pokeDexNumber = pokeDexNumber;
    }

}
