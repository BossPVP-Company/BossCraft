package com.bosspvp.api.gui.events;

import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiMenuClickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final InventoryClickEvent parentEvent;
    private final GuiMenu menu;
    private final GuiSlot slot;
    private boolean cancelled;

    public GuiMenuClickEvent(@NotNull Player player,
                             @NotNull GuiMenu menu,
                             @Nullable GuiSlot slot,
                             @NotNull InventoryClickEvent event) {
        super(player);
        this.menu = menu;
        this.slot = slot;
        parentEvent = event;
    }
    public @NotNull InventoryClickEvent getParentEvent() {
        return parentEvent;
    }

    public @NotNull GuiMenu getMenu() {
        return menu;
    }

    public @Nullable GuiSlot getClickedComponent() {
        return slot;
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
