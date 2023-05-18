package com.bosspvp.api.commands;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.LangSettings;
import com.bosspvp.api.exceptions.NotificationException;
import com.bosspvp.api.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BossCommand implements CommandBase, CommandExecutor, TabCompleter {

    private final BossPlugin plugin;

    @Getter
    private final String commandName;

    @Getter
    private final String requiredPermission;

    @Getter
    private final HashMap<String,CommandBase> subcommands;

    @Getter @Setter
    private boolean playersAllowed = true;
    @Getter @Setter
    private boolean consoleAllowed = true;

    /**
     * Create a new command.
     *
     * @param plugin      The plugin.
     * @param commandName The name used in execution.
     * @param permission  The permission required to execute the command.
     */
    protected BossCommand(@NotNull final BossPlugin plugin,
                          @NotNull final String commandName,
                          @Nullable final String permission) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.requiredPermission = permission;
        this.subcommands = new HashMap<>();
    }

    public final void register() {
        plugin.getLogger().info("Registering command "+commandName);
        PluginCommand command = Bukkit.getPluginCommand(this.getCommandName());
        if (command == null) {
            plugin.getLogger().info("Registering failed! Command not found. For Devs: Check the command list of plugin.yml");
            return;
        }

        command.setExecutor(this);
        command.setTabCompleter(this);

        command.setDescription(this.getDescription());

        List<String> aliases = new ArrayList<>(command.getAliases());
        aliases.addAll(this.getAliases());
        command.setAliases(aliases);

        addSubcommand(loadHelp());

    }

    public final void unregister() {
        PluginCommand command = Bukkit.getPluginCommand(this.getCommandName());
        if (command == null) return;

        command.unregister(getCommandMap());
    }
    /**
     * The default help command
     * <p></p>
     * Override if you want to make your own implementation
     *
     * @return  The help command
     */
    protected CommandBase loadHelp(){
        return (new BossSubcommand(getPlugin(),"help",this) {
            @Override
            protected void onCommandExecute(@NotNull CommandSender sender, @NotNull List<String> args) throws NotificationException {
                BossCommand command = (BossCommand) getParent();
                if(command.getSubcommands().size() == 0) {
                    sender.sendMessage(StringUtils.format("&c"+command.getUsage()+"&7 - &f"+command.getDescription()));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("&a------ ").append(getPlugin().getName()).append(" &a------").append("\n");
                for(CommandBase subcommand : subcommands.values()){
                    if(!subcommand.canExecute(sender,true)) continue;
                    sb.append("&c").append(subcommand.getUsage()).append("&7 - &f").append(subcommand.getDescription())
                            .append("\n");
                }
                sender.sendMessage(StringUtils.format(sb.toString()));
            }

            @Override
            protected @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull List<String> args) {
                return new ArrayList<>();
            }

            @Override
            public @NotNull String getDescription() {
                return "list of available commands";
            }

            @Override
            public @NotNull String getUsage() {
                return getParent().getUsage() + " help";
            }
        });
    }


    /**
     * Internal implementation of the Bukkit method
     *
     * @param sender  The executor of the command.
     * @param command The bukkit command.
     * @param label   The name of the executed command.
     * @param args    The arguments of the command (anything after the physical command name)
     * @return true if command proceeded by the Atum core
     */
    @Override
    public final boolean onCommand(@NotNull final CommandSender sender,
                                   @NotNull final Command command,
                                   @NotNull final String label,
                                   @NotNull final String[] args) {
        if (!command.getName().equalsIgnoreCase(this.getCommandName())) {
            getPlugin().getLogger().warning("onCommand executes inside the wrong class or " +
                    "the command name is wrongly specified! CommandName: " + getCommandName()
                    + "  Command name received: " + command.getName());
            return false;
        }

        try{
            handleCommand(sender, args);
        }catch (NotificationException e){
            sender.sendMessage(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            LangSettings langSettings = getPlugin().getLangSettings();
            sender.sendMessage(langSettings.getPrefix()+ langSettings.getErrorOnCommand());
        }
        return true;
    }

    /**
     * Handle the command.
     *
     * @param sender The sender.
     * @param args   The arguments.
     */
    @Override
    public final void handleCommand(@NotNull final CommandSender sender,
                                    @NotNull final String[] args) throws NotificationException {

        if (!canExecute(sender,true)) {
            return;
        }

        if (args.length > 0) {
            for (CommandBase subcommand : this.getSubcommands().values()) {
                if (!subcommand.getCommandName().equalsIgnoreCase(args[0])) continue;
                if (!subcommand.canExecute(sender,true)) return;

                subcommand.handleCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                return;

            }
        }
        onCommandExecute(sender, Arrays.asList(args));
    }

    /**
     * command execution.
     *
     * @param sender The command sender.
     * @param args   The command arguments
     */
    protected abstract void onCommandExecute(@NotNull CommandSender sender, @NotNull List<String> args) throws NotificationException;

    /**
     * Internal implementation of the Bukkit method
     * <p></p>
     *
     * @param sender  command sender
     * @param command command interface
     * @param label   command label
     * @param args    command args
     * @return suggested commands
     */
    @Override
    public final @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                      @NotNull final Command command,
                                                      @NotNull final String label,
                                                      @NotNull final String[] args) {
        if (!command.getName().equalsIgnoreCase(this.getCommandName())) {
            getPlugin().getLogger().warning("onTabComplete executes inside the wrong class or " +
                    "the command name is wrongly specified! CommandName: " + getCommandName()
                    + "  Command name received: " + command.getName());
            return null;
        }

        try {
            return this.handleTabCompletion(sender, args);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Handles the tab completion.
     *
     * @param sender The sender.
     * @param args   The arguments.
     * @return The tab completion results.
     */
    @Override
    public final List<String> handleTabCompletion(@NotNull final CommandSender sender,
                                                  @NotNull final String[] args) {

        if (!canExecute(sender,false)) return null;


        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0],
                    getSubcommands().values().stream()
                            .filter(subCommand -> subCommand.canExecute(sender,false))
                            .map(CommandBase::getCommandName)
                            .collect(Collectors.toList()),
                    completions
            );

            Collections.sort(completions);

            if (!completions.isEmpty()) {
                return completions;
            }
        } else if (args.length >= 2) {

            for (CommandBase subcommand : getSubcommands().values()) {
                if (!args[0].equalsIgnoreCase(subcommand.getCommandName())) continue;
                if (!subcommand.canExecute(sender,false)) return null;

                return subcommand.handleTabCompletion(sender,
                        Arrays.copyOfRange(args, 1, args.length));

            }


        }
        List<String> completions = onTabComplete(sender, Arrays.asList(args));
        if(completions==null) return StringUtil.copyPartialMatches(args[args.length-1],
                Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                        .collect(Collectors.toList()),
                new ArrayList<>());
        return completions;
    }

    /**
     * tab completion.
     *
     * @param sender The command sender.
     * @param args   The command arguments
     * @return Command suggestions
     */
    @Nullable
    protected abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull List<String> args);


    /**
     * Get the internal server CommandMap.
     *
     * @return The CommandMap.
     */
    public static CommandMap getCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new NullPointerException("Command map wasn't found!");
        }
    }

    @Override
    public @NotNull CommandBase addSubcommand(@NotNull CommandBase subcommand) {
        subcommands.put(subcommand.getCommandName(),subcommand);
        return subcommand;
    }


    private List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }

}
