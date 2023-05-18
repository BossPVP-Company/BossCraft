package com.bosspvp.api.commands;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.LangSettings;
import com.bosspvp.api.exceptions.NotificationException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface CommandBase {
    /**
     * Get command name.
     *
     * @return The name of the command.
     */
    @NotNull
    String getCommandName();

    /**
     * Get command description. Used in help method
     *
     * @return The description of a command
     */
    @NotNull
    String getDescription();

    /**
     * Get command usage. Used in help method
     *
     * @return The usage of a command
     */
    @NotNull
    String getUsage();

    /**
     * Get command permission.
     *
     * @return The permission of the command.
     */
    @Nullable
    String getRequiredPermission();


    /**
     * If console is allowed to use this command
     *
     * @return true if allowed.
     */
    boolean isConsoleAllowed();

    /**
     * If players are allowed to use this command
     *
     * @return true if allowed.
     */
    boolean isPlayersAllowed();

    /**
     * Checks whether the specified sender
     * is the player
     *
     * @return true if sender is a player
     */
    default boolean isPlayer(CommandSender sender){
        return sender instanceof Player;
    }
    /**
     * Checks whether the specified sender
     * is the RCON
     *
     * @return true if sender is a RCON
     */
    default boolean isRcon(CommandSender sender){
        return sender instanceof RemoteConsoleCommandSender;
    }

    /**
     * Add a subcommand to the command.
     *
     * @param subcommand The subcommand.
     * @return The parent command.
     */
    @NotNull
    CommandBase addSubcommand(@NotNull CommandBase subcommand);

    /**
     * Handle the command.
     *
     * @param sender The sender.
     * @param args   The arguments.
     */
    void handleCommand(@NotNull final CommandSender sender,
                       @NotNull final String[] args) throws NotificationException;
    /**
     * Handle the tab completion.
     *
     * @param sender The sender.
     * @param args   The arguments.
     * @return The tab completion results.
     */
    List<String> handleTabCompletion(@NotNull final CommandSender sender,
                                     @NotNull final String[] args);
    /**
     *
     * @param sender The command sender.
     * @param notifyOnFalse sends the message on false return
     * @return true if sender can execute the command
     */
    default boolean canExecute(CommandSender sender, boolean notifyOnFalse){

        boolean isPlayer=isPlayer(sender);
        LangSettings langSettings = getPlugin().getLangSettings();
        //console
        if (!isPlayer && !isConsoleAllowed()) {
            if(notifyOnFalse) sender.sendMessage(langSettings.getPrefix()+ langSettings.getPlayersOnly());
            return false;
        }
        if(!isPlayer) return true;
        //player
        if (!isPlayersAllowed()) {
            if(notifyOnFalse) sender.sendMessage(langSettings.getPrefix()+ langSettings.getConsoleOnly());
            return false;
        }
        if (getRequiredPermission()!=null&&!sender.hasPermission(getRequiredPermission())) {
            if(notifyOnFalse) sender.sendMessage(langSettings.getPrefix()+ langSettings.getNoPermission());
            return false;
        }

        return true;
    }

    /**
     * Basic notification method, that throws NotificationException
     * @param msg The msg to send
     */
    default void notify(@NotNull String msg) throws NotificationException {

        throw new NotificationException(msg);
    }
    /**
     * throws NotificationException when obj is null
     * @param obj an object to check
     * @param msg The msg to send
     */
    default @NotNull <T> T notifyNull(@Nullable T obj,
                                      @NotNull String msg)
            throws NotificationException {

        if (obj==null) notify(msg);


        assert obj != null;
        return obj;
    }
    /**
     * throws NotificationException when predicate#test(obj) returns false
     * @param obj an object to check
     * @param predicate the condition to test on the received object
     * @param msg The msg to send
     */
    default @NotNull <T> T notifyFalse(@NotNull T obj,
                                       @NotNull Predicate<T> predicate,
                                       @NotNull String msg) throws NotificationException {
        notifyFalse(predicate.test(obj), msg);
        return obj;
    }
    /**
     * throws NotificationException when condition is false
     * @param condition boolean value
     * @param msg The msg to send
     */
    default boolean notifyFalse(boolean condition,
                                @NotNull String msg) throws NotificationException {

        if (!condition) notify(msg);

        return true;
    }
    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();

}
