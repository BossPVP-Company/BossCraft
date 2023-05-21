package com.bosspvp.api.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.menu.GuiMenu;
import org.jetbrains.annotations.NotNull;

public interface GuiDrawer {

    /**
     * Open the menu synchronously
     * @param menu the menu
     */
    default void open(@NotNull GuiMenu menu){
        open(menu,false);
    }

    /**
     * Open the menu
     * @param menu the frame
     * @param async whether to open the frame asynchronously
     */
    void open(@NotNull GuiMenu menu, boolean async);

    /**
     * Update the menu Synchronously
     *
     * @param menu the frame
     */
    default void update(@NotNull GuiMenu menu){
        update(menu, false);
    }
    /**
     * Update the menu
     *
     * @param menu the frame
     * @param async whether to update the frame asynchronously
     */
    void update(@NotNull GuiMenu menu, boolean async);



    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();
}
