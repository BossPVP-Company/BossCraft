package com.bosspvp.api.gui.menu;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.tuples.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public interface GuiMenu {

    @NotNull String getTitle();
    int getRows();

    @NotNull List<GuiSlot> buildSlots();
    void refreshSlots();


    default @Nullable GuiSlot getSlotAt(int position, boolean playerInventory){
        for(GuiSlot slot : getSavedSlots(playerInventory)){
            if(Arrays.stream(slot.getSlotPositions()).anyMatch((it)->it==position)){
                return slot;
            }
        }
        return null;
    }
    @NotNull List<GuiSlot> getSavedSlots(boolean playerInventory);


    @NotNull Player getViewer();


    @NotNull
    static MenuBuilder builder(BossPlugin plugin, int rows){
        return BossAPI.getInstance().createMenuBuilder(plugin,rows);
    }


    @NotNull BossPlugin getPlugin();


    default void open(){
        getPlugin().getGuiController().getGuiDrawer().open(this);
    }


}
