package com.bosspvp.core.gui;

import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.gui.slot.SlotBuilder;
import com.bosspvp.api.gui.slot.SlotClickHandler;
import com.bosspvp.api.gui.slot.SlotUpdater;
import com.bosspvp.api.tuples.Pair;
import com.bosspvp.api.tuples.Triplet;
import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class BossSlotBuilder implements SlotBuilder {
    private ItemStack item;
    private final int[] slotPositions;
    private boolean onPlayerInventory;
    private Pair<Integer,SlotUpdater> slotUpdater;
    private final HashMap<ClickType, Triplet<String,Boolean,SlotClickHandler>> clickHandlers = new HashMap<>();

    public BossSlotBuilder(int ... slotPositions){
        this.slotPositions = slotPositions;
    }

    @Override
    public @NotNull SlotBuilder setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    @Override
    public @NotNull SlotBuilder setUpdater(int period,
                                           @NotNull SlotUpdater updater) {
        this.slotUpdater = new Pair<>(period,updater);
        return this;
    }

    @Override
    public @NotNull SlotBuilder placeOnPlayerInventory(boolean flag) {
        onPlayerInventory = flag;
        return this;
    }

    @Override
    public @NotNull SlotBuilder addClickHandler(@NotNull SlotClickHandler onClick,
                                                @Nullable String permission,
                                                boolean confirmation,
                                                @NotNull ClickType... clickTypes) {
        for(ClickType clickType : clickTypes){
            clickHandlers.put(clickType,new Triplet<>(permission,confirmation,onClick));
        }

        return this;
    }

    @Override
    public @NotNull GuiSlot build() {
        return new BossGuiSlot(
                slotPositions,
                item,
                onPlayerInventory,
                slotUpdater,
                clickHandlers
        );
    }
}
