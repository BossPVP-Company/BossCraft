package com.bosspvp.api.gui.slot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface SlotUpdater {

    @Nullable
    ItemStack update(@NotNull ItemStack previous);
}
