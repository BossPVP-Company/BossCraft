package com.bosspvp.core.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.events.GuiMenuOpenEvent;
import com.bosspvp.api.gui.GuiDrawer;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.menu.types.WarningMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.misc.BossDebugger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class BossGuiDrawer implements GuiDrawer {
    private final BossPlugin plugin;
    private final BossGuiController guiController;

    private final ConcurrentHashMap<UUID, GuiMenu> openingFrame = new ConcurrentHashMap<>();
    public BossGuiDrawer(BossPlugin plugin, BossGuiController guiController){
        this.plugin = plugin;
        this.guiController = guiController;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private boolean debug = false;

    @Override
    public void open(GuiMenu menu, boolean async) {
        UUID uuid = menu.getViewer().getUniqueId();
        if (menu == openingFrame.get(uuid)) return;
        openingFrame.put(uuid,menu);
        Runnable task = ()->{
            Player viewer = menu.getViewer();
            try {
                new BossDebugger(
                        plugin,
                        "OpenFrame",
                        "&eFrameTitle:&6 "+menu.getTitle()+"\n&eViewer:&6 "+menu.getViewer().getName()
                ).start(()->{
                    Inventory inventory = prepareInventory(menu);
                    if (menu != openingFrame.get(uuid)) return;
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        GuiMenuOpenEvent event = new GuiMenuOpenEvent(viewer, menu);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            viewer.openInventory(inventory);
                            guiController.registerMenu(menu);
                        }
                    });
                    openingFrame.remove(uuid);
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                if (!(menu instanceof WarningMenu)) {
                    open(
                            new WarningMenu(
                                    plugin,
                                    null,
                                    viewer,
                                    "&8&oUnhandled error,\n&8&o please contact with server administration"
                            ),
                            async
                    );
                } else viewer.closeInventory();
            }
        };
        if(async){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }else {
            Bukkit.getScheduler().runTask(plugin,task);
        }
    }

    public void update(@NotNull GuiMenu menu, boolean async) {
        Runnable task = () -> {
            Player viewer = menu.getViewer();

            try {
                new BossDebugger(
                        plugin,
                        "UpdateFrame",
                        "&eFrameTitle:&6 "+menu.getTitle()+" " +
                                "&eViewer:&6 "+menu.getViewer().getName()
                ).start(()-> {
                    try {
                        setSlots(viewer.getOpenInventory().getTopInventory(), menu);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

            } catch (Exception ex) {
                ex.printStackTrace();
                if (!(menu instanceof WarningMenu)) {
                    open(
                            new WarningMenu(
                                    plugin,
                                    null,
                                    viewer,
                                    "&8&oUnhandled error,\n&8&o please contact with server administration"
                            ),
                            async
                    );
                } else viewer.closeInventory();
            }
        };
        if(async){
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }else {
            Bukkit.getScheduler().runTask(plugin,task);
        }
    }


    private Inventory prepareInventory(GuiMenu menu) {
        Inventory inventory = Bukkit.createInventory(menu.getViewer(),
                menu.getRows()*9, Component.text(menu.getTitle()));
        long start = System.currentTimeMillis();
        setSlots(inventory, menu);
        if (debug) {
            plugin.getLogger().info(
                    String.format("It took %s " +
                            "millisecond(s) to load the menu" +
                            " %s for %s",
                            System.currentTimeMillis() - start,
                            menu.getTitle(),
                            menu.getViewer().getName())

            );
        }
        return inventory;
    }

    private void setSlots(Inventory inventory, GuiMenu menu) {
        inventory.clear();
        List<GuiSlot> slots = menu.buildSlots();
        if (slots.isEmpty()) {
            plugin.getLogger().warning("Menu "+menu.getTitle()+" is empty");
            return;
        }
        for (GuiSlot slot : slots) {
            for(int pos : slot.getSlotPositions()) {
                if (slot.isOnPlayerInventory()) {
                    if (pos >= menu.getViewer().getInventory().getSize()) continue;
                    menu.getViewer().getInventory().setItem(pos, slot.getItem());

                } else {
                    if (pos >= menu.getRows() * 9) continue;
                    inventory.setItem(pos, slot.getItem());
                }
            }
        }
    }



    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }
}
