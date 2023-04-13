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
import com.cobblemon.mod.common.api.pokemon.evolution.Evolution;
import com.cobblemon.mod.common.pokemon.Pokemon;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
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

        double percentage = (double) total.get() * (double) pokemonData.size() * 100.0D;
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

    public List<String> spawnBiomes(Pokemon pokemon)
    {
        List<String> strings = new ArrayList<>();

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
            String s = "&7%name% &e-> &7%description%"
                    .replace("%name%", potentialAbility.getTemplate().getName()
                            .replace("%description%", potentialAbility.getTemplate().getDescription())
                    );
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

    public GooeyButton filler = GooeyButton.builder()
            .display(new ItemStack(Blocks.GRAY_STAINED_GLASS_PANE, 1))
            .build();

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
                .onClick(b -> UIManager.openUIForcefully(b.getPlayer(), progressionMainMenu()))
                .display(new ItemStack(Items.ENCHANTED_BOOK)).build();

        PlaceholderButton placeHolderButton = new PlaceholderButton();

        Template template = ChestTemplate.builder(6)
                .border(0, 0, 6, 9, filler)
                .set(0, 3, previous)
                .set(0, 4, progressionView)
                .set(0, 5, next)
                .rectangle(1, 0, 5, 9, placeHolderButton)
                .build();

        return PaginationHelper.createPagesFromPlaceholders(template, pokemonButtonEntries(), LinkedPage.builder().title(Util.formattedString("&cPoke&fDex")).template(template));
    }

    public GooeyPage pokemonPage(DexPokemon dexPokemon) {
        ChestTemplate.Builder templateBuilder = ChestTemplate.builder(6)
                .border(0, 0, 6, 9, filler);

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
                .lore(baseStats(pokemon))
                .display(new ItemStack(CobblemonItems.MYSTIC_WATER.get()))
                .build();

        templateBuilder.set(1, 1, abilities);
        templateBuilder.set(1, 3, evolutions);
        templateBuilder.set(1, 5, learnMoves);
        templateBuilder.set(1, 7, tmMoves);
        templateBuilder.set(2, 2, eggMoves);
        templateBuilder.set(2, 4, evoMoves);
        templateBuilder.set(2, 6, baseStats);

        return GooeyPage.builder().template(templateBuilder.build()).build();
    }

    public GooeyPage progressionMainMenu() {
        ChestTemplate.Builder templateBuilder = ChestTemplate.builder(6)
                .border(0, 0, 6, 9, filler);

        CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.forEach((s, configurableDexProgression) -> {
            DexProgression dexProgression = dexProgressionList.get(s);

            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString(dexProgression.getGuiTitle()))
                    .display(new ItemStack(Items.PAPER))
                    .lore(Util.formattedArrayList(dexProgression.getGuiLore()))
                    .onClick(b -> {
                        if (dexProgression.isClaimed())
                            return;
                        if (!completedPercent(dexProgression))
                            return;
                        dexProgression.claimRewards(b.getPlayer().getName().getString());
                    })
                    .build();
            templateBuilder.set(configurableDexProgression.getGuiSlot(), button);
        });
        return GooeyPage.builder().template(templateBuilder.build()).build();
    }

    public void openDex(ServerPlayer player) {
        UIManager.openUIForcefully(player, mainUI());
    }
}
