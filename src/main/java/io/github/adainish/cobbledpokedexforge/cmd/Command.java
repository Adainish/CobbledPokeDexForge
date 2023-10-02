package io.github.adainish.cobbledpokedexforge.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.adainish.cobbledpokedexforge.CobbledPokeDexForge;
import io.github.adainish.cobbledpokedexforge.obj.Player;
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
                        Player dexPlayer = CobbledPokeDexForge.playerStorage.getPlayer(player.getUUID());
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
                .then(Commands.literal("migrate")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                        .executes(cc -> {
                            if (CobbledPokeDexForge.dbConfig.enabled) {
                                if (CobbledPokeDexForge.playerStorage.database != null) {
                                    try {
                                        Util.send(cc.getSource().source, "&eNow attempting migration... This could be very heavy. Please do not do this with any players online!");
                                        CobbledPokeDexForge.playerStorage.database.migratePlayerData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Util.send(cc.getSource().source, "&cSomething went wrong while migrating, please check your console for errors!");
                                    }
                                } else {
                                    Util.send(cc.getSource().source, "&cWe failed to retrieve the database! This isn't good");
                                }
                            } else {
                                Util.send(cc.getSource().source, "&cThe database is marked as disabled in the config!");
                            }
                            return 1;
                        })
                )
                ;
    }
}
