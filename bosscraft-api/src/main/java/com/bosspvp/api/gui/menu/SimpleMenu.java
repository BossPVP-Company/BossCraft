package com.bosspvp.api.gui.menu;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.slot.GuiSlot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public abstract class SimpleMenu implements GuiMenu{
    @Getter
    private final BossPlugin plugin;
    @Getter
    private final Player viewer;
    @Getter
    private final int rows;
    private List<GuiSlot> savedSlots;


    public SimpleMenu(@NotNull BossPlugin plugin,
                      @NotNull Player viewer,
                      int rows){
        this.plugin = plugin;
        this.viewer = viewer;
        this.rows = rows;
        savedSlots = new ArrayList<>();
    }

    @Override
    public @NotNull List<GuiSlot> buildSlots() {
        onBuildSlots();
        return savedSlots;
    }

    protected abstract void onBuildSlots();


    public void addSlot(GuiSlot slot){
        savedSlots.add(slot);
    }

    @Override
    public void refreshSlots() {

    }

    @Override
    public @NotNull List<GuiSlot> getSavedSlots(boolean playerInventory) {
        return savedSlots.stream().filter(it->it.isOnPlayerInventory()==playerInventory)
                .collect(Collectors.toList());
    }
}
