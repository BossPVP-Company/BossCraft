package com.bosspvp.api.commands;

import com.bosspvp.api.BossPlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BossSubcommand extends BossCommand{
    @Getter
    public final CommandBase parent;

    /**
     * Create a new subcommand.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param permission  The permission
     * @param parent      The parent of a subcommand
     */
    protected BossSubcommand(@NotNull BossPlugin plugin, @NotNull String commandName, @Nullable String permission, @NotNull CommandBase parent) {
        super(plugin, commandName, permission);
        this.setConsoleAllowed(parent.isConsoleAllowed());
        this.setPlayersAllowed(parent.isPlayersAllowed());
        this.parent=parent;
    }

    /**
     * Create a new subcommand.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param parent      The parent of a subcommand
     */
    protected BossSubcommand(@NotNull BossPlugin plugin, @NotNull String commandName, @NotNull CommandBase parent) {
        this(plugin,commandName,parent.getRequiredPermission(),parent);
    }
}
