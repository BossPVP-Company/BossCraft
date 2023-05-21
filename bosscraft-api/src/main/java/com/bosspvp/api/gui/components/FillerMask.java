package com.bosspvp.api.gui.components;

import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.inventories.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FillerMask implements GuiComponent {
    private final GuiSlot[][] mask;

    private final int rows;
    private final int columns;

    private final boolean isOnPlayerInventory;

    public FillerMask(@NotNull final ItemStack[] items,
                      boolean isOnPlayerInventory,
                      @NotNull final String... pattern) {
        this.isOnPlayerInventory = isOnPlayerInventory;

        rows = pattern.length;
        columns = pattern[0].length();
        mask = new GuiSlot[rows][columns];

        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = items[i];

            int row = 0;

            for (String patternRow : pattern) {
                int column = 0;
                for (char c : patternRow.toCharArray()) {
                    if (c == '0') {
                        mask[row][column] = null;
                    } else if (c == Character.forDigit(i + 1, 10)) {
                        mask[row][column] = GuiSlot.builder(column+row*columns).setItem(itemStack).build();
                    }

                    column++;
                }
                row++;
            }
        }
    }

    public GuiSlot[][] getMask() {
        return this.mask;
    }

    @Override
    public int getRowsSize() {
        return rows;
    }

    @Override
    public int getColumnsSize() {
        return columns;
    }

    @Override
    public boolean isOnPlayerInventory() {
        return false;
    }

    @Override
    public @Nullable GuiSlot getSlotAt(final int row,
                                       final int column) {
        return mask[row][column];
    }
}
