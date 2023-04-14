package io.github.adainish.cobbledpokedexforge.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfigurableDexProgression
{
    private String id;
    private List<String> rewards = new ArrayList<>();
    private double percentage = 0;
    private int guiSlot = 1;
    private String guiItem = "minecraft:paper";
    private String guiTitle = "&bDex Progression %amount%";
    private List<String> guiLore = new ArrayList<>();

    public ConfigurableDexProgression(String id)
    {
        this.setId(id);
        this.setPercentage(0);
        this.setGuiItem("minecraft:paper");
        this.setGuiTitle("&bDex Progression");
        this.setGuiLore(new ArrayList<>());
        this.setRewards(new ArrayList<>());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public void setRewards(List<String> rewards) {
        this.rewards = rewards;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getGuiItem() {
        return guiItem;
    }

    public void setGuiItem(String guiItem) {
        this.guiItem = guiItem;
    }

    public String getGuiTitle() {
        return guiTitle.replace("%amount%", String.valueOf(getPercentage()));
    }

    public void setGuiTitle(String guiTitle) {
        this.guiTitle = guiTitle;
    }

    public List<String> getGuiLore() {
        List<String> formatted = new ArrayList<>();
        for (String s:guiLore) {
            formatted.add(s.replace("%amount%", String.valueOf(getPercentage())));
        }
        return formatted;
    }

    public void setGuiLore(List<String> guiLore) {
        this.guiLore = guiLore;
    }

    public int getGuiSlot() {
        return guiSlot;
    }

    public void setGuiSlot(int guiSlot) {
        this.guiSlot = guiSlot;
    }
}
