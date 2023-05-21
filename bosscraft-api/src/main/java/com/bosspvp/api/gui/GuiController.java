package com.bosspvp.api.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.menu.GuiMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GuiController extends Listener {

    /**
     * turn on / turn off
     * components updater
     * <p></p>
     * By default, it is disabled
     *
     * @param flag  true / false
     */
    void enableUpdater(boolean flag);

    /**
     *
     * @return  is component updater enabled?
     */
    boolean isUpdaterEnabled();

    /**
     * Register the menu
     *
     * @param menu the menu
     */
    void registerMenu(@NotNull GuiMenu menu);


    /**
     * Unregister all frames
     *
     */
    void unregisterAllMenus();

    /**
     * Unregister the menu
     *
     * @param menu the menu
     */
    void unregisterMenu(@NotNull GuiMenu menu);


    /**
     * Get registered menus
     *
     * @return registered frames
     */
    @NotNull
    List<GuiMenu> getRegisteredMenus();

    /**
     * Returns frame opened by the player
     *
     * @param viewer the frame viewer
     * @return the menu
     */
    @Nullable
    GuiMenu getPlayerCurrentMenu(@NotNull Player viewer);

    /**
     * Get GuiDrawer
     *
     * @return The GuiDrawer
     */
    @NotNull
    GuiDrawer getGuiDrawer();

    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
