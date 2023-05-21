package com.bosspvp.api.gui.slot;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.gui.GuiComponent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GuiSlot extends GuiComponent {

    @NotNull
    ItemStack getItem();

    boolean update();
    @Nullable SlotClickHandler getClickHandler(@NotNull ClickType clickType);
    @Nullable String requiredPermission(@NotNull ClickType clickType);
    boolean requiresConfirmation(@NotNull ClickType clickType);

    //added temporarily
    @Deprecated
    void setSlotPositions(int ... slotPositions);
    int[] getSlotPositions();

    @Override
    default int getRowsSize(){
        return 1;
    }
    @Override
    default int getColumnsSize(){
        return 1;
    }

    @Override
    @Nullable
    default GuiSlot getSlotAt(final int row, final int column) {
        return this;
    }

    //added temporarily
    @Deprecated
    @NotNull
    GuiSlot clone();
    static SlotBuilder builder(int ... positions){
        return BossAPI.getInstance().createSlotBuilder(positions);
    }
}
