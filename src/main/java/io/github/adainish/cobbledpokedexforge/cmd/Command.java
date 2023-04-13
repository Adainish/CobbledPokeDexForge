package io.github.adainish.cobbledpokedexforge.cmd;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.adainish.cobbledpokedexforge.obj.Player;
import io.github.adainish.cobbledpokedexforge.storage.PlayerStorage;
import io.github.adainish.cobbledpokedexforge.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class Command
{
    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("pokedex")
                .executes(cc -> {
                    try {
                        ServerPlayer player = cc.getSource().getPlayerOrException();
                        Player dexPlayer = PlayerStorage.getPlayer(player.getUUID());
                        if (dexPlayer != null)
                            dexPlayer.pokeDex.openDex(player);
                    } catch (CommandSyntaxException e)
                    {
                        cc.getSource().sendSystemMessage(Component.literal(Util.formattedString("&cOnly a player may run this command!")));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                })
//
//                .then(Commands.literal("admin")
//                        .executes(cc -> {
//                            try {
//                                ServerPlayer player = cc.getSource().getPlayerOrException();
//                                UIManager.openUIForcefully(player, AdminViewPoolGUI.AdminPoolView());
//                            } catch (CommandSyntaxException e)
//                            {
//                                cc.getSource().sendSystemMessage(Component.literal(Util.formattedString("&cOnly a player may run this command!")));
//                            }
//                            return Command.SINGLE_SUCCESS;
//                        })
//                )
//                .then(Commands.literal("reload")
//                        .executes(cc -> {
//                            CobbledWonderTradeForge.getInstance().reload();
//                            return Command.SINGLE_SUCCESS;
//                        })
//                )
                ;
    }
}
