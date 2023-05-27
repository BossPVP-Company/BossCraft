package com.bosspvp.core.gui;

import com.bosspvp.api.gui.slot.GuiSlot;
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

public class BossGuiSlot implements GuiSlot {
    @Getter
    private ItemStack item;
    @Getter
    private int[] slotPositions;
    @Getter
    private final boolean onPlayerInventory;

    private SlotUpdater slotUpdater;
    private final HashMap<ClickType, Triplet<String,Boolean,SlotClickHandler>> clickHandlers;

    private int updatePeriod;
    private int timer;


    public BossGuiSlot(int[] slotPositions,
                       @NotNull ItemStack item,
                       boolean onPlayerInventory,
                       @Nullable Pair<Integer,SlotUpdater> slotUpdater,
                       @NotNull HashMap<ClickType, Triplet<String,Boolean,SlotClickHandler>> clickHandlers
    ){

        this.slotPositions = slotPositions;
        this.item = item;
        this.onPlayerInventory = onPlayerInventory;
        this.clickHandlers = clickHandlers;

        if(slotUpdater!=null) {
            this.slotUpdater = slotUpdater.getSecond();
            this.updatePeriod = slotUpdater.getFirst();
        }
    }

    @Override
    public boolean update() {
        if(slotUpdater==null) return false;
        updatePeriod++;
        if(updatePeriod>=timer){
            item = slotUpdater.update(item);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable SlotClickHandler getClickHandler(@NotNull ClickType clickType) {
        Triplet<String,Boolean,SlotClickHandler> triplet = clickHandlers.get(clickType);
        if(triplet == null) return null;
        return triplet.getThird();
    }

    @Override
    public @Nullable String requiredPermission(@NotNull ClickType clickType) {
        Triplet<String,Boolean,SlotClickHandler> triplet = clickHandlers.get(clickType);
        if(triplet == null) return null;
        return triplet.getFirst();
    }

    @Override
    public boolean requiresConfirmation(@NotNull ClickType clickType) {
        Triplet<String,Boolean,SlotClickHandler> triplet = clickHandlers.get(clickType);
        if(triplet == null) return false;
        return Boolean.TRUE.equals(triplet.getSecond());
    }

    @Override
    public void setSlotPositions(int... slotPositions) {
        this.slotPositions = slotPositions;
    }

    @Override
    public @NotNull GuiSlot clone() {
        try {
            return (GuiSlot) super.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

}
