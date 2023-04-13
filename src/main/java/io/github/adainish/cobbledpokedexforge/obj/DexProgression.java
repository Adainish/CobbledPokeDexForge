package io.github.adainish.cobbledpokedexforge.obj;

import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DexProgression
{
    private String id;
    private List<String> rewards = new ArrayList<>();
    private boolean claimed = false;
    public DexProgression(String id)
    {
        this.setId(id);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRewards() {
        return CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.get(id).getRewards();
    }

    public double getPercentage() {
        return CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.get(id).getPercentage();
    }

    public String getGuiItem() {
        return CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.get(id).getGuiItem();
    }

    public String getGuiTitle() {
        return CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.get(id).getGuiTitle();
    }


    public List<String> getGuiLore() {
        return CobbledPokeDexForge.dexProgressionConfig.configurableDexProgressions.get(id).getGuiLore();
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public void claimRewards(String playerName)
    {
        if (claimed)
            return;

        for (String s:rewards) {
            Reward r = CobbledPokeDexForge.rewardsConfig.rewardHashMap.get(s);
            if (r != null)
            {
                r.commands.forEach(s1 -> {
                    Util.runCommand(s.replace("%pl%", playerName));
                });
            }
        }
    }
}
