package com.bosspvp.api.gui.slot;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SlotBuilder {


    @NotNull SlotBuilder setItem(ItemStack item);
    @NotNull SlotBuilder setUpdater(int period, @NotNull SlotUpdater updater);

    @NotNull SlotBuilder placeOnPlayerInventory(boolean flag);

    default @NotNull SlotBuilder addClickHandler(@NotNull SlotClickHandler onClick,
                                @NotNull ClickType ... clickTypes){
        addClickHandler(onClick,null,false,clickTypes);
        return this;
    }
    default @NotNull SlotBuilder addClickHandler(@NotNull SlotClickHandler onClick,
                                boolean confirmation,
                                @NotNull ClickType ... clickTypes){
        addClickHandler(onClick,null,confirmation,clickTypes);
        return this;
    }
    default @NotNull SlotBuilder addClickHandler(@NotNull SlotClickHandler onClick,
                                @NotNull String permission,
                                @NotNull ClickType ... clickTypes){
        addClickHandler(onClick,permission,false,clickTypes);
        return this;
    }
    @NotNull SlotBuilder addClickHandler(@NotNull SlotClickHandler onClick,
                                @Nullable String permission,
                                boolean confirmation,
                                @NotNull ClickType ... clickTypes);

    @NotNull GuiSlot build();
}
