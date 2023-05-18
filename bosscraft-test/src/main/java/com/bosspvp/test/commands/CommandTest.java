package com.bosspvp.test.commands;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.commands.BossCommand;
import com.bosspvp.api.exceptions.NotificationException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandTest extends BossCommand {

    public CommandTest(@NotNull BossPlugin plugin) {
        super(plugin, "test", null);

        addSubcommand(new SubcommandPlugin(plugin,this));
    }

    @Override
    protected void onCommandExecute(@NotNull CommandSender sender, @NotNull List<String> args) throws NotificationException {
        executeHelp(sender,args);
    }

    @Override
    protected @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull List<String> args) {
        return null;
    }

    @Override
    public @NotNull String getDescription() {
        return "test command";
    }

    @Override
    public @NotNull String getUsage() {
        return "/test";
    }
}
