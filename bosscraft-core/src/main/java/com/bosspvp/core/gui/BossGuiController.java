package com.bosspvp.core.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.events.GuiMenuClickEvent;
import com.bosspvp.api.gui.events.GuiMenuCloseEvent;
import com.bosspvp.api.gui.events.GuiMenuDragEvent;
import com.bosspvp.api.gui.GuiController;
import com.bosspvp.api.gui.GuiDrawer;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.menu.types.ConfirmationMenu;
import com.bosspvp.api.gui.menu.types.WarningMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.*;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossGuiController implements GuiController, Runnable {
    private final BossPlugin plugin;

    private ConcurrentHashMap<UUID, GuiMenu> registeredFrames;
    private BossGuiDrawer guiDrawer;
    private boolean updaterEnabled = false;
    private BukkitTask task = null;


    public BossGuiController(@NotNull BossPlugin plugin){
        this.plugin = plugin;
        registeredFrames = new ConcurrentHashMap<>();
        guiDrawer = new BossGuiDrawer(plugin, this);
    }

    public void run() {
        for(GuiMenu menu : registeredFrames.values()){
            menu.refreshSlots();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() != plugin) return;
        unregisterAllMenus();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onClose(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player player)) return;
        GuiMenu menu = registeredFrames.get(player.getUniqueId());
        if(menu==null) return;
        try {
            GuiMenuCloseEvent ev = new GuiMenuCloseEvent(player, menu);
            Bukkit.getPluginManager().callEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        registeredFrames.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrag(InventoryDragEvent event){
        if(!(event.getWhoClicked() instanceof Player player)) return;
        GuiMenu frame = registeredFrames.get(player.getUniqueId());
        if(frame==null) return;
        GuiMenuDragEvent ev = new GuiMenuDragEvent(
                player,
                frame,
                event
        );
        Bukkit.getPluginManager().callEvent(ev);
        event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;
        Inventory clickedInventory = event.getClickedInventory();
        GuiMenu menu = registeredFrames.get(player.getUniqueId());
        if(menu == null) return;

        event.setResult(Event.Result.DENY);
        event.setCursor(null);

        if(clickedInventory == null) return;
        //frame click event
        GuiSlot slot = menu.getSlotAt(event.getSlot(),
                clickedInventory.getType() == InventoryType.PLAYER
        );
        if(slot == null) return;
        GuiMenuClickEvent ev = new GuiMenuClickEvent(
                player,
                menu,
                slot,
                event
        );
        Bukkit.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) return;


        ClickType click = event.getClick();
        var clickHandler = slot.getClickHandler(click);
        Runnable action = clickHandler!=null ? ()-> clickHandler.handle(slot) : null;
        String permission = slot.requiredPermission(click);
        if (permission != null && !player.hasPermission(permission)) {
            new WarningMenu(plugin, menu, player, "lack of permission").open();
            return;
        }
        if (clickHandler!=null && slot.requiresConfirmation(click)) {
            action = () -> {
                new ConfirmationMenu(
                        plugin,
                        menu,
                        menu.getViewer(),
                        ()->clickHandler.handle(slot)
                ).open();
            };
        }
        Runnable finalListener = action;
        Bukkit.getScheduler().runTask(plugin, ()-> {
            try {
                if(finalListener == null) return;
                if(event.getCurrentItem()==null) return;
                finalListener.run();
            } catch (Exception e) {
                e.printStackTrace();
                if (!(menu instanceof WarningMenu)) {
                    new WarningMenu(
                            plugin, null, player,
                            "&8&oUnhandled error,\n" +
                                    "&8&o please contact with server administration"
                    ).open();
                } else player.closeInventory();
            }
        });
    }

    @Override
    public void enableUpdater(boolean flag) {
        updaterEnabled = flag;
        BukkitTask localTask = task;
        if(updaterEnabled) {
            if(localTask == null || localTask.isCancelled()){
                task = Bukkit.getScheduler().runTaskTimer(plugin,
                        this,
                        0,
                        1
                );
            }
        }else{
            if(localTask != null && !localTask.isCancelled()){
                localTask.cancel();
            }
            task = null;
        }
    }

    @Override
    public boolean isUpdaterEnabled() {
        return updaterEnabled;
    }


    @Override
    public void registerMenu(@NotNull GuiMenu menu) {
        registeredFrames.put(menu.getViewer().getUniqueId(),menu);
    }

    @Override
    public void unregisterAllMenus() {
        for (GuiMenu frame : registeredFrames.values()) {
            frame.getViewer().closeInventory();
        }
        System.gc();
    }

    @Override
    public void unregisterMenu(GuiMenu menu) {
        menu.getViewer().closeInventory();
    }

    @Override
    public @NotNull List<GuiMenu> getRegisteredMenus() {
        return registeredFrames.values().stream().toList();
    }
    @Override
    public GuiMenu getPlayerCurrentMenu(@NotNull Player player) {
        return registeredFrames.get(player.getUniqueId());
    }

    @Override
    public GuiDrawer getGuiDrawer() {
        return guiDrawer;
    }

    @Override
    public BossPlugin getPlugin() {
        return plugin;
    }
}
