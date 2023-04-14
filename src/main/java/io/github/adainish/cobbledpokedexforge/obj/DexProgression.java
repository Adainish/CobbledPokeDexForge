package io.github.adainish.cobbledpokedexforge.obj;

import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.util.Util;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class DexProgression
{
    private String id;
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

    public List<Reward> getRewardsList()
    {
        List<Reward> rewardsList = new ArrayList<>();
        for (String s:getRewards()) {
            if (CobbledPokeDexForge.rewardsConfig.rewardHashMap.containsKey(s))
                rewardsList.add(CobbledPokeDexForge.rewardsConfig.rewardHashMap.get(s));
        }
        return rewardsList;
    }

    public void claimRewards(ServerPlayer player)
    {
        if (claimed)
            return;

        for (Reward r:getRewardsList()) {
            r.giveRewards(player);
        }
        this.claimed = true;
    }
}
