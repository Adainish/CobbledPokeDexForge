package io.github.adainish.cobbledpokedexforge.obj;

import io.github.adainish.cobbledpokedexforge.util.Util;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class Reward
{
    public String identifier;
    public String guiString = "";
    public List<String> guiLore = new ArrayList<>();
    public String itemID = "minecraft:paper";
    public List<String> commands = new ArrayList<>();

    public Reward(String identifier)
    {
        this.identifier = identifier;
        this.commands = new ArrayList<>();
    }

    public void giveRewards(ServerPlayer serverPlayer)
    {
        //run commands
        for (String s:commands) {
            Util.runCommand(s.replace("%pl%", serverPlayer.getName().getString()));
        }
    }
}
