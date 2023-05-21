package com.bosspvp.core.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class BossGuiMenu implements GuiMenu {
    private HashMap<Integer, List<GuiComponent>> components;
    private List<GuiSlot> savedSlots = new ArrayList<>();
    @Getter
    private final BossPlugin plugin;
    @Getter
    private final Player viewer;
    @Getter
    private String title;
    @Getter
    private int rows;

    public BossGuiMenu(@NotNull BossPlugin plugin,
                       @NotNull Player viewer,
                       @NotNull String title,
                       int rows,
                       @NotNull HashMap<Integer, List<GuiComponent>> components){
        this.plugin = plugin;
        this.viewer = viewer;
        this.title = title;
        this.rows = Math.max(rows, 1);
        this.components = components;
    }


    @Override
    public @NotNull List<GuiSlot> buildSlots() {
        List<GuiSlot> slots = new ArrayList<>();
        GuiSlot[] out = new GuiSlot[rows*9];
        for(int position : components.keySet()){
            int row = position/9;
            int column = position - row*9;
            List<GuiComponent> list = components.get(position);
            if(list == null) continue;
            for(int i = list.size()-1; i>=0;i--){ //layers. Starts from foreground
                GuiComponent component = list.get(i);
                if(component==null) continue;
                for(int columnStep = 0; columnStep<component.getColumnsSize();columnStep++){

                    for(int rowStep= 0; rowStep<component.getRowsSize();rowStep++){
                        //getting the menu pos
                        int var1 = (column + columnStep);
                        if(var1>=9) continue; // out of bounds
                        var1 = (row + rowStep);
                        if(var1>=rows) continue; // out of bounds
                        int menuPos = position + columnStep + rowStep*9;

                        if(out[menuPos] != null) continue; //already filled this slot

                        //getting the slot
                        GuiSlot slot = component.getSlotAt(rowStep,columnStep);
                        out[menuPos] = slot;
                        //@TODO change this mess (have no idea yet how to restructure GuiSlot for that)
                        if(slot==null) continue;
                        slot = slot.clone();
                        slot.setSlotPositions(menuPos);
                        slots.add(slot);
                    }
                }
            }
        }
        savedSlots = slots;
        return slots;
    }

    @Override
    public void refreshSlots() {
        for(GuiSlot slot : savedSlots){
            if(slot.update()){
                Inventory inventory  = slot.isOnPlayerInventory() ?
                        getViewer().getInventory() : getViewer().getOpenInventory().getTopInventory();

                for (int pos : slot.getSlotPositions()) {
                    inventory.setItem(pos, slot.getItem());
                }
            }
        }

    }

    @Override
    public @NotNull List<GuiSlot> getSavedSlots(boolean playerInventory) {
        return savedSlots.stream().filter(it->it.isOnPlayerInventory()==playerInventory)
                .collect(Collectors.toList());
    }


}
