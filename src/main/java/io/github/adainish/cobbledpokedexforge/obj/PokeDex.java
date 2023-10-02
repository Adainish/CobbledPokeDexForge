package io.github.adainish.cobbledpokedexforge.obj;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.Template;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.evolution.Evolution;
import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools;
import com.cobblemon.mod.common.api.spawning.detail.PokemonSpawnDetail;
import com.cobblemon.mod.common.pokemon.Pokemon;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.storage.PlayerStorage;
import io.github.adainish.cobbledpokedexforge.util.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PokeDex {
    public HashMap<Integer, DexPokemon> pokemonData = new HashMap<>();
    public HashMap<String, DexProgression> dexProgressionList = new HashMap<>();

    public PokeDex() {

    }

    public double getCompletionPercentage(int decimalPlaces) {
        AtomicInteger total = new AtomicInteger();

        pokemonData.forEach((integer, dexPokemon) -> {
            if (dexPokemon.registered)
                total.set(total.get() + 1);
        });

        double percentage = (double) total.get() / (double) PokemonSpecies.INSTANCE.getImplemented().size() * 100.0D;
        if (!CobbledPokeDexForge.configurableDexConfig.checkByImplemented)
            percentage = (double) total.get() / (double) PokemonSpecies.INSTANCE.getSpecies().size() * 100.0D;
        return Math.floor(percentage * Math.pow(10.0D, decimalPlaces)) / Math.pow(10.0D, decimalPlaces);
    }

    public boolean completedPercent(DexProgression dexProgression) {
        return getCompletionPercentage(1) >= dexProgression.getPercentage();
    }

    public List<String> eggMoveList(Pokemon pokemon)
    {
        List<String> list = new ArrayList<>();

        pokemon.getSpecies().getMoves().getEggMoves().forEach(moveTemplate -> list.add(moveTemplate.getName()));

        return list;
    }

    public List<String> evoMoveList(Pokemon pokemon)
    {
        List<String> list = new ArrayList<>();

        pokemon.getSpecies().getMoves().getEvolutionMoves().forEach(moveTemplate -> list.add(moveTemplate.getName()));

        return list;
    }

    public List<String> levelUpMoveList(Pokemon pokemon)
    {
        List<String> list = new ArrayList<>();

        pokemon.getSpecies().getMoves().getLevelUpMoves().forEach((integer, moveTemplates) -> moveTemplates.forEach(moveTemplate -> list.add("&7Lvl %lvl%: &e%move%"
                        .replace("%lvl%", String.valueOf(integer))
                .replace("%move%", moveTemplate.getName()))));

        return list;
    }

    public List<String> evolutions(Pokemon pokemon)
    {
        List<String> list = new ArrayList<>();

        for (Evolution evolution : pokemon.getEvolutions()) {
            if (evolution.getResult().getSpecies() != null) {
                list.add("&e%species%".replace("%species%", evolution.getResult().getSpecies()));
            }
        }

        return list;
    }

    public List<PokemonSpawnDetail> getSpawnDetails(Pokemon pokemon)
    {
        List<PokemonSpawnDetail> spawnDetails = new ArrayList<>();
        CobblemonSpawnPools.INSTANCE.getWORLD_SPAWN_POOL().iterator().forEachRemaining(spawnDetail -> {
            if (spawnDetail instanceof PokemonSpawnDetail)
            {
                PokemonSpawnDetail pokemonSpawnDetail = (PokemonSpawnDetail) spawnDetail;
                if (pokemonSpawnDetail.getPokemon().matches(pokemon)) {
                    spawnDetails.add(pokemonSpawnDetail);
                }
            }
        });
        return spawnDetails;
    }

    public List<String> spawnData(Pokemon pokemon)
    {
        List<String> strings = new ArrayList<>();

        CobblemonSpawnPools.INSTANCE.getWORLD_SPAWN_POOL().iterator().forEachRemaining(spawnDetail -> {
            if (spawnDetail instanceof PokemonSpawnDetail)
            {
                PokemonSpawnDetail pokemonSpawnDetail = (PokemonSpawnDetail) spawnDetail;
                if (pokemonSpawnDetail.getPokemon().matches(pokemon)) {
                    strings.add("&7Level Range: %first% - %last%"
                            .replace("%first%", String.valueOf(pokemonSpawnDetail.getLevelRange().getFirst()))
                            .replace("%last%", String.valueOf(pokemonSpawnDetail.getLevelRange().getLast()))
                    );
                    if (pokemonSpawnDetail.getCompositeCondition() != null) {
                        pokemonSpawnDetail.getCompositeCondition().getConditions().forEach(spawningCondition -> {
                            spawningCondition.getTimeRange().getRanges().forEach(intRange -> {
                                strings.add("Time Range: %start% - %finish%"
                                        .replace("%start%", String.valueOf(intRange.getStart()))
                                        .replace("%finish%", String.valueOf(intRange.getLast()))
                                );
                            });
                            spawningCondition.getBiomes().forEach(biomeRegistryLikeCondition -> {

                            });
                        });
                    }
//                    if (!pokemonSpawnDetail.getDrops().getEntries().isEmpty()) {
//                        strings.add("&bPossible Drops:");
//                        pokemonSpawnDetail.getDrops().getEntries().forEach(dropEntry -> {
//                            strings.add("%item% drop chance: %percentage%%"
//                                            .replace("%item%", dropEntry.)
//                                    .replace("%percentage%", String.valueOf(dropEntry.getPercentage())));
//                        });
//                    } else {
//
//                    }
                }
            }
        });
        return strings;
    }


    public List<String> tmList(Pokemon pokemon)
    {
        List<String> list = new ArrayList<>();

        pokemon.getSpecies().getMoves().getTmMoves().forEach(moveTemplate -> list.add(moveTemplate.getName()));

        return list;
    }

    public List<String> baseStats(Pokemon pokemon)
    {
        List<String> list = new ArrayList<>();

        pokemon.getSpecies().getBaseStats().forEach((stat, integer) -> list.add("&b%stat% &e-> &7%integer%"
                .replace("%stat%", stat.getDisplayName().getString())
                .replace("%integer%", String.valueOf(integer))
        ));

        return list;
    }

    public List<String> abilityList(Pokemon pokemon) {
        List<String> list = new ArrayList<>();
        pokemon.getSpecies().getAbilities().forEach(potentialAbility -> {
            String s = "&7%name%"
                    .replace("%name%", potentialAbility.getTemplate().getName());
            list.add(Util.formattedString(s));
        });
        return list;
    }

    public List<String> basePokemonDataInfo(DexPokemon dexPokemon) {
        List<String> lore = new ArrayList<>();
        for (String s : Arrays.asList("%seen%",
                "%caught%")) {
            lore.add(s
                    .replace("%caught%", dexPokemon.registeredStatus())
                    .replace("%seen%", dexPokemon.seenStatus())
            );
        }
        return lore;
    }

    public List<Button> pokemonButtonEntries() {
        List<Button> buttons = new ArrayList<>();

        pokemonData.forEach((integer, dexPokemon) -> {
            Pokemon pokemon = dexPokemon.getPokemon();
            GooeyButton.Builder buttonBuilder = GooeyButton.builder();
            buttonBuilder.display(Util.returnIcon(pokemon));
            buttonBuilder.title(Util.formattedString("&b%species% #%number%"
                            .replace("%species%", pokemon.getSpecies().getName()))
                    .replace("%number%", String.valueOf(dexPokemon.pokeDexNumber))
            );
            buttonBuilder.lore(Util.formattedArrayList(basePokemonDataInfo(dexPokemon)));
            buttonBuilder.onClick(b ->
            {
                if (dexPokemon.registered) {
                    UIManager.openUIForcefully(b.getPlayer(), pokemonPage(dexPokemon));
                }
            });
            buttons.add(buttonBuilder.build());
        });

        return buttons;
    }

    public GooeyButton filler() {
        return GooeyButton.builder()
                .display(new ItemStack(Blocks.GRAY_STAINED_GLASS_PANE, 1))
                .build();
    }

    public LinkedPage mainUI() {
        // TODO: 13/04/2023 Add Generation Sorting
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title("Previous Page")
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title("Next Page")
                .linkType(LinkType.Next)
                .build();

        GooeyButton progressionView = GooeyButton.builder()
                .title(Util.formattedString("&aPokeDex Progression"))
                .lore(Util.formattedArrayList(Arrays.asList("&aCurrent Dex Progress %amount%%".replace("%amount%", String.valueOf(getCompletionPercentage(1))))))
                .onClick(b -> UIManager.openUIForcefully(b.getPlayer(), progressionMainMenu()))
                .display(new ItemStack(Items.ENCHANTED_BOOK)).build();

        PlaceholderButton placeHolderButton = new PlaceholderButton();

        Template template = ChestTemplate.builder(6)
                .border(0, 0, 6, 9, filler())
                .set(0, 3, previous)
                .set(0, 4, progressionView)
                .set(0, 5, next)
                .rectangle(1, 0, 5, 9, placeHolderButton)
                .build();

        return PaginationHelper.createPagesFromPlaceholders(template, pokemonButtonEntries(), LinkedPage.builder().title(Util.formattedString("&cPoke&fDex")).template(template));
    }

    public GooeyPage pokemonPage(DexPokemon dexPokemon) {
        ChestTemplate.Builder templateBuilder = ChestTemplate.builder(6)
                .border(0, 0, 6, 9, filler());

        Pokemon pokemon = dexPokemon.getPokemon();
        GooeyButton abilities = GooeyButton.builder()
                .display(new ItemStack(CobblemonItems.CLOVER_SWEET.get()))
                .title(Util.formattedString("&eAbilities:"))
                .lore(Util.formattedArrayList(abilityList(pokemon)))
                .build();

        GooeyButton learnMoves = GooeyButton.builder()
                .title(Util.formattedString("&dLevel Up Moves"))
                .lore(Util.formattedArrayList(levelUpMoveList(pokemon)))
                .display(new ItemStack(CobblemonItems.RARE_CANDY.get()))
                .build();

        GooeyButton tmMoves = GooeyButton.builder()
                .title(Util.formattedString("&cTM Moves"))
                .lore(Util.formattedArrayList(tmList(pokemon)))
                .display(new ItemStack(CobblemonItems.MUSCLE_BAND.get()))
                .build();

        GooeyButton eggMoves = GooeyButton.builder()
                .title(Util.formattedString("&bEgg Moves"))
                .lore(Util.formattedArrayList(eggMoveList(pokemon)))
                .display(new ItemStack(CobblemonItems.LUCKY_EGG.get()))
                .build();

        GooeyButton evoMoves = GooeyButton.builder()
                .title(Util.formattedString("&cEvolution Moves"))
                .lore(Util.formattedArrayList(evoMoveList(pokemon)))
                .display(new ItemStack(CobblemonItems.MAGMARIZER.get()))
                .build();

        GooeyButton evolutions = GooeyButton.builder()
                .title(Util.formattedString("&dEvolutions"))
                .lore(Util.formattedArrayList(evolutions(pokemon)))
                .display(new ItemStack(CobblemonItems.THUNDER_STONE.get()))
                .build();

        GooeyButton baseStats = GooeyButton.builder()
                .title(Util.formattedString("&bBase Stats"))
                .lore(Util.formattedArrayList(baseStats(pokemon)))
                .display(new ItemStack(CobblemonItems.MYSTIC_WATER.get()))
                .build();

        GooeyButton spawnData = GooeyButton.builder()
                .title(Util.formattedString("&aSpawn Info"))
                .lore(Util.formattedArrayList(spawnData(pokemon)))
                .display(new ItemStack(CobblemonItems.SPELL_TAG.get()))
                .build();

        GooeyButton rewardsButton = GooeyButton.builder()
                .title(Util.formattedString("&6Pokemon Rewards"))
                .display(new ItemStack(Items.ENDER_CHEST))
                .lore(Util.formattedArrayList(Arrays.asList("&eClaim rewards for registering this Pokemon")))
                .onClick(b -> {
                    Player player = CobbledPokeDexForge.playerStorage.getPlayer(b.getPlayer().getUUID());
                    if (player != null) {
                        Util.send(b.getPlayer(), "&cYou've claimed your rewards!");
                        dexPokemon.claimRewards(b.getPlayer());
                        player.pokeDex = this;
                        player.save();
                    } else {
                        Util.send(b.getPlayer(), "&cSomething went wrong loading your data");
                    }
                    UIManager.closeUI(b.getPlayer());
                })
                .build();
        templateBuilder.set(0, 0, backToMainPageButton());
        templateBuilder.set(1, 1, abilities);
        templateBuilder.set(1, 3, evolutions);
        templateBuilder.set(1, 5, learnMoves);
        templateBuilder.set(1, 7, tmMoves);
        templateBuilder.set(2, 2, eggMoves);
        templateBuilder.set(2, 4, evoMoves);
        templateBuilder.set(2, 6, baseStats);
        templateBuilder.set(3, 1, spawnData);
        if (dexPokemon.registered && !dexPokemon.getRewards().isEmpty() && !dexPokemon.claimed)
        {
            templateBuilder.set(3, 3, rewardsButton);
        }

        return GooeyPage.builder().template(templateBuilder.build()).build();
    }

    public GooeyButton backToMainPageButton()
    {
        return GooeyButton.builder()
                .title(Util.formattedString("&3Go Back"))
                .onClick(b -> {
                    UIManager.openUIForcefully(b.getPlayer(), mainUI());
                })
                .display(new ItemStack(Items.ARROW))
                .build();
    }

    public GooeyPage progressionMainMenu() {
        ChestTemplate.Builder templateBuilder = ChestTemplate.builder(6)
                .border(0, 0, 6, 9, filler());

        templateBuilder.set(0, 0, backToMainPageButton());
        CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.forEach((s, configurableDexProgression) -> {
            DexProgression dexProgression = dexProgressionList.get(s);
            if (dexProgression != null) {
                try {
                    GooeyButton button = GooeyButton.builder()
                            .title(Util.formattedString(configurableDexProgression.getGuiTitle()))
                            .display(new ItemStack(Items.PAPER))
                            .lore(Util.formattedArrayList(configurableDexProgression.getGuiLore()))
                            .onClick(b -> {
                                if (dexProgression.isClaimed()) {
                                    Util.send(b.getPlayer(), "&cYou've already claimed these rewards");
                                    return;
                                }
                                if (!completedPercent(dexProgression)) {
                                    Util.send(b.getPlayer(), "&cYou've not reached the required amount");
                                    return;
                                }

                                Player player = CobbledPokeDexForge.playerStorage.getPlayer(b.getPlayer().getUUID());
                                if (player != null) {
                                    Util.send(b.getPlayer(), "&cYou've claimed your rewards!");
                                    dexProgression.claimRewards(b.getPlayer());
                                    player.pokeDex = this;
                                    player.save();
                                } else {
                                    Util.send(b.getPlayer(), "&cSomething went wrong loading your data");
                                }
                                UIManager.closeUI(b.getPlayer());
                            })
                            .build();
                    templateBuilder.set(configurableDexProgression.getGuiSlot(), button);
                } catch (Exception e) {
                    CobbledPokeDexForge.getLog().error("An issue was detected in the Progression GUI");
                    CobbledPokeDexForge.getLog().error(e);
                }
            }
        });
        return GooeyPage.builder().template(templateBuilder.build()).title(Util.formattedString("&aCurrent Dex Progress %amount%%".replace("%amount%", String.valueOf(getCompletionPercentage(1))))).build();
    }

    public void openDex(ServerPlayer player) {
        UIManager.openUIForcefully(player, mainUI());
    }
}
