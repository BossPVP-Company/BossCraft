package com.bosspvp.api.commands;

import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.*;

import java.util.logging.Level;

@Data
public class CommandRegistry {
/* @TODO
    private final Plugin plugin;
    private final Set<BossCommand> registeredCommands;
    public CommandRegistry(Plugin plugin) {
        this.plugin = plugin;
        this.registeredCommands = new HashSet<>();
    }

    public void register(BossCommand baseCommand) throws FailedCommandRegistration {
        unregister(baseCommand, false);
        SimpleCommandMap commandMap;
        try {
            commandMap = getCommandMap();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FailedCommandRegistration(e.getMessage());
        }
        Map<String, Command> knownCommands;
        try {
            knownCommands = getKnownCommands(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FailedCommandRegistration(e.getMessage());
        }

        unregisterOldCommands(baseCommand, commandMap, knownCommands);
        commandMap.register(plugin.getDescription().getName(), baseCommand);
        registeredCommands.add(baseCommand);
        plugin.getLogger().log(Level.INFO, "Successfully registered '" + baseCommand.getName() + "' command!");
    }

    public void registerAll(BaseCommand... baseCommands) throws FailedCommandRegistration {
        for (BossCommand command : baseCommands)
            register(command);
        plugin.getLogger().log(Level.INFO, "Successfully registered all commands!");
    }

    public void unregisterAll() throws FailedCommandRegistration {
        for (BossCommand registeredCommand : new HashSet<>(registeredCommands))
            unregister(registeredCommand);
        plugin.getLogger().log(Level.INFO, "Successfully unregistered all commands!");
    }

    private void unregister(BossCommand baseCommand) throws FailedCommandRegistration {
        unregister(baseCommand, true);
    }

    private void unregister(BossCommand baseCommand, boolean message) throws FailedCommandRegistration {
        SimpleCommandMap commandMap;
        try {
            commandMap = getCommandMap();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FailedCommandRegistration(e.getMessage());
        }
        Map<String, Command> knownCommands;
        try {
            knownCommands = getKnownCommands(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FailedCommandRegistration(e.getMessage());
        }

        unregisterOldCommands(baseCommand, commandMap, knownCommands);

        registeredCommands.remove(baseCommand);
        if (message)
            plugin.getLogger().log(Level.INFO, "Successfully unregistered '" + baseCommand.geCo + "' command!");
    }

    private SimpleCommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Field commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        return (SimpleCommandMap) commandMapField.get(pluginManager);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getKnownCommands(SimpleCommandMap commandMap) throws NoSuchFieldException, IllegalAccessException {
        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);
        return (Map<String, Command>) knownCommandsField.get(commandMap);
    }

    private void unregisterOldCommands(BossCommand baseCommand, SimpleCommandMap commandMap, Map<String, Command> knownCommands) {
        commandMap.getCommands().stream()
                .filter(command -> command.getName().equals(baseCommand.getCommandName()))
                .forEach(command -> command.unregister(commandMap));

        new HashMap<>(knownCommands).keySet().stream()
                .filter(s -> baseCommand.getCommandName().equals(s) || baseCommand.getAliases().contains(s))
                .forEach(knownCommands::remove);
    }

 */

}