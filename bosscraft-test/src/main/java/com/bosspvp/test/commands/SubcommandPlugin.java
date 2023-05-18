package com.bosspvp.test.commands;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.commands.BossSubcommand;
import com.bosspvp.api.commands.CommandBase;
import com.bosspvp.api.exceptions.NotificationException;
import com.bosspvp.api.utils.PluginUtils;
import com.bosspvp.api.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SubcommandPlugin extends BossSubcommand {
    protected SubcommandPlugin(@NotNull BossPlugin plugin, @NotNull CommandBase parent) {
        super(plugin, "plugin", parent);
    }

    @Override
    protected void onCommandExecute(@NotNull CommandSender sender, @NotNull List<String> args) throws NotificationException {
        notifyFalse(!args.isEmpty(), getHelp());

        switch (args.get(0).toLowerCase()){
            case "list" -> {
                var plugins = PluginUtils.getLoadedPlugins();
                var sb = new StringBuilder();
                sb.append(ChatColor.GRAY)
                        .append(String.format("Loaded Plugins(%s): \n", plugins.size()));
                for(String plugin : PluginUtils.getLoadedPlugins()){
                    sb.append(plugin).append(", ");
                }
                sender.sendMessage(sb.toString());
            }
            case "info" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                sender.sendMessage(PluginUtils.getPluginInfo(args.get(1)));
            }
            case "usage" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                var plugin = PluginUtils.getLoadedPlugin(args.get(1));
                notifyNull(plugin,"&cThe specified plugin not found");

                sender.sendMessage(PluginUtils.getPluginUsage(plugin));
            }
            case "enable" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                var plugin = PluginUtils.getLoadedPlugin(args.get(1));
                notifyNull(plugin,"&cThe specified plugin not found");

                PluginUtils.enablePlugin(plugin);
                sender.sendMessage(ChatColor.GREEN+"Successfully enabled the plugin "+plugin.getName());
            }
            case "disable" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                var plugin = PluginUtils.getLoadedPlugin(args.get(1));
                notifyNull(plugin,"&cThe specified plugin not found");

                PluginUtils.disablePlugin(plugin);
                sender.sendMessage(ChatColor.GREEN+"Successfully disabled the plugin "+plugin.getName());
            }
            case "load" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                var plugin = PluginUtils.loadPlugin(args.get(1));

                sender.sendMessage(ChatColor.GREEN+"Successfully loaded the plugin "+plugin.getName());
            }
            case "unload" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                var plugin = PluginUtils.getLoadedPlugin(args.get(1));
                notifyNull(plugin,"&cThe specified plugin not found");

                PluginUtils.unloadPlugin(plugin);

                sender.sendMessage(ChatColor.GREEN+"Successfully unloaded the plugin "+plugin.getName());
            }
            case "restart" -> {
                notifyFalse(args.size() > 1, "&cSpecify the plugin");
                var plugin = PluginUtils.getLoadedPlugin(args.get(1));
                notifyNull(plugin,"&cThe specified plugin not found");

                PluginUtils.unloadPlugin(plugin);
                PluginUtils.loadPlugin(plugin.getName());

                sender.sendMessage(ChatColor.GREEN+"Successfully restarted the plugin "+plugin.getName());
            }
            default -> {
                sender.sendMessage(getHelp());
            }
        }
    }

    @Override
    protected @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull List<String> args) {
        return null;
    }

    @Override
    public @NotNull String getDescription() {
        return "manage plugins";
    }

    @Override
    public @NotNull String getUsage() {
        return "/test plugin [load/unload/enable/disable/info/list]";
    }

    private String getHelp() {
        return StringUtils.format(
                """
                    &aAvailable commands:
                    &c/atum plugin list &7-&f sends the list of all loaded plugins
                    &c/atum plugin info [plugin name] &7-&f sends the plugin version, its authors and current status
                    &c/atum plugin usage [plugin name] &7-&f sends all commands plugin registered
                    &c/atum plugin enable [plugin name] &7-&f enables the plugin
                    &c/atum plugin disable [plugin name] &7-&f disables the plugin
                    &c/atum plugin load [plugin name] &7-&f loads the plugin from /plugins folder
                    &c/atum plugin unload [plugin name] &7-&f unloads the plugin
                    &c/atum plugin restart [plugin name] &7-&f unloads and loads the plugin
                    """
        );
    }
}
