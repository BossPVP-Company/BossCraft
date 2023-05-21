package com.bosspvp.api.gui.events;

import com.bosspvp.api.gui.menu.GuiMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class GuiMenuDragEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final InventoryDragEvent parentEvent;
    private final GuiMenu menu;
    private boolean cancelled;

    public GuiMenuDragEvent(@NotNull Player player,
                            @NotNull GuiMenu menu,
                            @NotNull InventoryDragEvent event) {
        super(player);
        this.menu = menu;
        parentEvent = event;
    }
    public @NotNull InventoryDragEvent getParentEvent() {
        return parentEvent;
    }

    public @NotNull GuiMenu getMenu() {
        return menu;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
