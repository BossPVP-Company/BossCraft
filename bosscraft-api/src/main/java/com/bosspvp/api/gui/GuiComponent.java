package com.bosspvp.api.gui;

import com.bosspvp.api.gui.slot.GuiSlot;
import org.jetbrains.annotations.Nullable;

public interface GuiComponent extends Cloneable {

    int getRowsSize();
    int getColumnsSize();

    boolean isOnPlayerInventory();


    @Nullable
    default GuiSlot getSlotAt(final int row,
                              final int column) {
        return null;
    }

}
