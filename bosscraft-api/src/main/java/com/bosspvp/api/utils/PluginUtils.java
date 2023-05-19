package com.bosspvp.api.utils;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.exceptions.NotificationException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;

/**
 * PluginUtils
 * <p></p>
 * Replacement of PlugMan
 */
public class PluginUtils {
    @SuppressWarnings({"removal"})
    public static PluginLoader getPluginLoader() {
        return Bukkit.getPluginManager().getPlugins()[0].getPluginLoader();
    }

    public static File getPluginsDirectory() {
        return new File(".", "plugins");
    }


    @NotNull
    public static List<String> getLoadedPlugins() {
        String pluginName;
        StringBuilder list = new StringBuilder();
        ArrayList<String> pluginList = new ArrayList<>();
        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            pluginName = (pl.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + pl.getDescription().getName();
            pluginList.add(pluginName);
        }
        pluginList.sort(String.CASE_INSENSITIVE_ORDER);
        for (String name : pluginList) {
            if (list.length() > 0) list.append(ChatColor.WHITE).append(", ");
            list.append(name);
        }
        return pluginList;
    }

    public static Plugin getLoadedPlugin(@NotNull String pluginName) {
        for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
            if (!pl.getDescription().getName().equalsIgnoreCase(pluginName)) continue;
            return pl;
        }
        return null;
    }

    @NotNull
    public static String getPluginInfo(@NotNull String pluginName) {
        StringBuilder info = new StringBuilder();
        Plugin targetPlugin = getLoadedPlugin(pluginName);
        if (targetPlugin == null) {
            return ChatColor.RED + "&cThe specified plugin not found";
        }
        info.append("Plugin Info: ").append(ChatColor.GREEN)
                .append(targetPlugin.getName()).append("\n");
        info.append(ChatColor.GREEN).append("Version: ").append(ChatColor.GRAY)
                .append(targetPlugin.getDescription().getVersion()).append("\n");
        info.append(ChatColor.GREEN).append("Authors: ").append(ChatColor.GRAY)
                .append(targetPlugin.getDescription().getAuthors()).append("\n");
        info.append(ChatColor.GREEN).append("Status: ").append(targetPlugin.isEnabled()
                ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled");
        if (targetPlugin instanceof BossPlugin) {
            info.append("\n").append(ChatColor.GOLD).append(ChatColor.BOLD)
                    .append("This lovely plugin uses BossAPI!");
        }

        return info.toString();
    }

    public static boolean enablePlugin(@NotNull final Plugin plugin) {
        if (plugin.isEnabled()) return false;

        Bukkit.getPluginManager().enablePlugin(plugin);
        return true;

    }

    public static boolean disablePlugin(@NotNull final Plugin plugin) {
        if (!plugin.isEnabled()) return false;
        Bukkit.getPluginManager().disablePlugin(plugin);
        return true;

    }

    @NotNull
    public static String getPluginUsage(@NotNull final Plugin plugin) {
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> pluginCommands = new ArrayList<>();
        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();
        for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {
            pluginCommands.add(entry.getKey());
        }
        if (pluginCommands.isEmpty()) {
            return ChatColor.RED + "The plugin has no registered commands";
        }
        StringBuilder usage = new StringBuilder();
        usage.append(ChatColor.GRAY).append("Command List: \n");
        for (String cmd : pluginCommands) {
            usage.append(ChatColor.GREEN).append("\"").append(cmd).append("\"").append("\n");
        }
        usage.deleteCharAt(usage.length() - 1);
        return usage.toString();
    }


    public static Plugin loadPlugin(@NotNull final String name) throws NotificationException {
        Plugin target = null;

        File pluginsDir = new File("plugins");

        if (!pluginsDir.isDirectory()) {
            throw new NotificationException("&cPlugins directory doesn't exists");
        }

        File pluginFile = new File(pluginsDir, name + ".jar");
        if(!pluginFile.exists()||!pluginFile.isFile()){
            boolean found = false;
            for (File f : pluginsDir.listFiles()) {
                if (f.getName().endsWith(".jar")) {
                    try {
                        @SuppressWarnings({"removal"})
                        PluginDescriptionFile desc = getPluginLoader().getPluginDescription(f);
                        if (desc.getName().equalsIgnoreCase(name)) {
                            pluginFile = f;
                            found = true;
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(!found){
                throw new NotificationException("&cThe Plugin not found");
            }
        }
        boolean paperLoaded = false;
        try {
            Class paper = Class.forName("io.papermc.paper.plugin.manager.PaperPluginManagerImpl");
            Object paperPluginManagerImpl = paper.getMethod("getInstance").invoke(null);

            Field instanceManagerF = paperPluginManagerImpl.getClass().getDeclaredField("instanceManager");
            instanceManagerF.setAccessible(true);
            Object instanceManager = instanceManagerF.get(paperPluginManagerImpl);

            Method loadMethod = instanceManager.getClass().getMethod("loadPlugin", Path.class);
            loadMethod.setAccessible(true);
            target = (Plugin) loadMethod.invoke(instanceManager, pluginFile.toPath());

            Method enableMethod = instanceManager.getClass().getMethod("enablePlugin", Plugin.class);
            enableMethod.setAccessible(true);
            enableMethod.invoke(instanceManager, target);

            paperLoaded = true;
        } catch (Exception ignore) {
        } // Paper most likely not loaded

        if (!paperLoaded) {
            try {
                target = Bukkit.getPluginManager().loadPlugin(pluginFile);
            } catch (InvalidDescriptionException e) {
                e.printStackTrace();
                throw new NotificationException("&cThe Plugin has invalid description");
            } catch (InvalidPluginException e) {
                e.printStackTrace();
                throw new NotificationException("&cThe Plugin is invalid");
            }

            target.onLoad();
            Bukkit.getPluginManager().enablePlugin(target);
        }
        try {
            Method syncCommands = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]+ ".CraftServer")
                    .getDeclaredMethod("syncCommands");
            syncCommands.setAccessible(true);

            syncCommands.invoke(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return target;
    }


    public static void unloadPlugin(@NotNull final Plugin plugin) throws NotificationException {
        String name = plugin.getName();

        PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap;
        List<Plugin> plugins;
        Map<String, Plugin> lookupNames;
        Map<String, Command> commands;
        Map<Event, SortedSet<RegisteredListener>> listeners;

        try {

            Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            plugins = (List<Plugin>) pluginsField.get(pluginManager);

            Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            lookupNames = (Map<String, Plugin>) lookupNamesField.get(pluginManager);

            try {
                Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
                listenersField.setAccessible(true);
                listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
            } catch (Exception e) {
                listeners = null;
            }
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            commands = (Map<String, Command>) knownCommandsField.get(commandMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotificationException("&cFailed to unload the plugin");
        }

        pluginManager.disablePlugin(plugin);

        if (plugins != null) plugins.remove(plugin);
        if (lookupNames != null) lookupNames.remove(name);

        if (listeners != null) {
            for (SortedSet<RegisteredListener> set : listeners.values())
                set.removeIf(value -> value.getPlugin() == plugin);
        }
        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand c) {
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.updateCommands();
            }
        }

        ClassLoader cl = plugin.getClass().getClassLoader();

        if (cl instanceof URLClassLoader) {

            try {

                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);

                Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);

                ((URLClassLoader) cl).close();

            } catch (Exception ex) {
                ex.printStackTrace();
                throw new NotificationException("&cFailed to unload the plugin");
            }

        }
        try {

            Class paper = Class.forName("io.papermc.paper.plugin.manager.PaperPluginManagerImpl");
            Object paperPluginManagerImpl = paper.getMethod("getInstance").invoke(null);

            Field instanceManagerField = paperPluginManagerImpl.getClass().getDeclaredField("instanceManager");
            instanceManagerField.setAccessible(true);
            Object instanceManager = instanceManagerField.get(paperPluginManagerImpl);

            Field lookupNamesField = instanceManager.getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            Map<String, Object> lookupNames1 = (Map<String, Object>) lookupNamesField.get(instanceManager);

            Method disableMethod = instanceManager.getClass().getMethod("disablePlugin", Plugin.class);
            disableMethod.setAccessible(true);
            disableMethod.invoke(instanceManager, plugin);

            lookupNames1.remove(plugin.getName().toLowerCase());

            Field pluginListField = instanceManager.getClass().getDeclaredField("plugins");
            pluginListField.setAccessible(true);
            List<Plugin> pluginList = (List<Plugin>) pluginListField.get(instanceManager);
            pluginList.remove(plugin);

        } catch (Exception ignore) {
        } // Paper most likely not loaded

        // Will not work on processes started with the -XX:+DisableExplicitGC flag
        System.gc();
    }

    private PluginUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
