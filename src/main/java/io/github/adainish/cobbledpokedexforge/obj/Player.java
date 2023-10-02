package io.github.adainish.cobbledpokedexforge.obj;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.api.storage.pc.PCStore;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import org.bson.Document;

import java.util.*;

public class Player
{
    public UUID uuid;
    public PokeDex pokeDex;

    public Player(UUID uuid)
    {
        this.uuid = uuid;
        this.pokeDex = new PokeDex();
    }

    // Convert Player to Document
    public Document toDocument() {
        Document document = new Document();
        document.put("uuid", uuid.toString()); // Convert UUID to String for storage
        document.put("pokedex", pokeDex);
        return document;
    }

    public void saveNoCache()
    {
        CobbledPokeDexForge.playerStorage.savePlayerNoCache(this);
    }

    public void save()
    {
        //save to storage file
        CobbledPokeDexForge.playerStorage.savePlayer(this);
    }

    public void registerFromStorage()
    {
        List<Integer> alreadyScanned = new ArrayList<>();
        List<Integer> unSeen = new ArrayList<>();
        List<Integer> unRegistered = new ArrayList<>();
        this.pokeDex.pokemonData.forEach((integer, dexPokemon) -> {
            if (dexPokemon.registered && dexPokemon.seen)
                return;
            if (!dexPokemon.seen)
                unSeen.add(dexPokemon.pokeDexNumber);
            if (!dexPokemon.registered)
                unRegistered.add(dexPokemon.pokeDexNumber);
        });
        PlayerPartyStore playerPartyStore = null;
        PCStore pcStore = null;
        try {
            playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(uuid);
            pcStore = Cobblemon.INSTANCE.getStorage().getPC(uuid);
        } catch (NoPokemonStoreException e) {
            throw new RuntimeException(e);
        }

        for (Iterator<Pokemon> pokemonIterator : Arrays.asList(playerPartyStore.iterator(), pcStore.iterator())) {
            pokemonIterator.forEachRemaining(pokemon -> {
                if (alreadyScanned.contains(pokemon.getSpecies().getNationalPokedexNumber()))
                    return;
                boolean seen = false;
                boolean caught = false;
                if (unSeen.contains(pokemon.getSpecies().getNationalPokedexNumber()))
                {
                    seen = true;
                }
                if (unRegistered.contains(pokemon.getSpecies().getNationalPokedexNumber()))
                {
                    caught = true;
                }
                register(pokemon, seen, caught);
            });
        }

    }

    public void syncWithConfigurable()
    {
        CobbledPokeDexForge.configurableDex.syncConfigData(this);
        List<String> toRemoveProgressions = new ArrayList<>();
        pokeDex.dexProgressionList.forEach((s, dexProgression) -> {
            if (CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.get(s) != null)
                return;
            toRemoveProgressions.add(s);
        });
        toRemoveProgressions.stream().filter(s -> pokeDex.dexProgressionList.get(s) != null).forEach(s -> pokeDex.dexProgressionList.remove(s));

        CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.forEach((s, configurableDexProgression) -> {
            if (!pokeDex.dexProgressionList.containsKey(s))
            {
                DexProgression dexProgression = new DexProgression(s);
                pokeDex.dexProgressionList.put(s, dexProgression);
            }
        });
    }

    public void register(PokemonEntity pokemon, boolean seen, boolean caught)
    {
        register(pokemon.getPokemon().getSpecies(), seen, caught);
    }

    public void register(Pokemon pokemon, boolean seen, boolean caught)
    {
        register(pokemon.getSpecies(), seen, caught);
    }

    public void register(Species species, boolean seen, boolean caught)
    {
        DexPokemon dexPokemon;
        if (pokeDex.pokemonData.containsKey(species.getNationalPokedexNumber()))
        {
            dexPokemon = pokeDex.pokemonData.get(species.getNationalPokedexNumber());
        } else {
            dexPokemon = new DexPokemon(species.getNationalPokedexNumber());
        }
        if (dexPokemon.seen && dexPokemon.registered)
            return;
        if (seen && !dexPokemon.seen) dexPokemon.seen = true;
        if (caught)
        {
            if (!dexPokemon.registered)
            {
                dexPokemon.registered = true;
            }
        }
        this.pokeDex.pokemonData.put(dexPokemon.pokeDexNumber, dexPokemon);
    }

    public void updateCache() {
        CobbledPokeDexForge.wrapper.playerHashMap.put(uuid, this);
    }
}
