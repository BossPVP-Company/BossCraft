package com.bosspvp.api.gui.events;

import com.bosspvp.api.gui.menu.GuiMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class GuiMenuCloseEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final @NotNull GuiMenu menu;

    public GuiMenuCloseEvent(@NotNull Player viewer, @NotNull GuiMenu menu) {
        super(viewer);
        this.menu = menu;
    }

    public @NotNull GuiMenu getMenu() {
        return menu;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
