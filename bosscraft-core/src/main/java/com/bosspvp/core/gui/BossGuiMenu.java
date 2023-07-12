package com.bosspvp.core.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.GuiLayer;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.tuples.Pair;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class BossGuiMenu implements GuiMenu {
    private HashMap<Integer, GuiComponent[]> components;
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
                       @NotNull HashMap<Integer, GuiComponent[]> components){
        this.plugin = plugin;
        this.viewer = viewer;
        this.title = title;
        this.rows = Math.max(rows, 1);
        this.components = components;
    }


    @Override
    public @NotNull List<GuiSlot> buildSlots() {
        GuiSlot[] out = new GuiSlot[rows*9];
        int[] outFilledLayers = new int[rows*9];
        for(Map.Entry<Integer, GuiComponent[]> entry : components.entrySet()){
            int position = entry.getKey();
            int row = position/9;
            int column = position - row*9;
            GuiComponent[] list = entry.getValue();
            if(list == null) continue;
            for(int i = list.length - 1; i >= 0; i--){ //layers. Starts from foreground
                GuiComponent component = list[i];
                if(component == null) continue;
                for(int columnStep = 0; columnStep < component.getColumnsSize(); columnStep++){

                    for(int rowStep = 0; rowStep < component.getRowsSize(); rowStep++){
                        //getting the menu pos
                        int var1 = (column + columnStep);
                        if(var1 >= 9) continue; // out of bounds
                        var1 = (row + rowStep);
                        if(var1 >= rows) continue; // out of bounds
                        int menuPos = position + columnStep + rowStep * 9;

                        //already filled this with higher or same layer
                        if(out[menuPos] != null && outFilledLayers[menuPos] > i) continue;

                        //getting the slot
                        GuiSlot slot = component.getSlotAt(rowStep,columnStep);
                        if(slot==null) continue;
                        //@TODO change this mess (have no idea yet how to restructure GuiSlot for that)
                        slot = slot.clone();
                        slot.setSlotPositions(menuPos);
                        out[menuPos] = slot;
                        outFilledLayers[menuPos] = i;
                    }
                }
            }
        }
        savedSlots = Arrays.stream(out).filter(Objects::nonNull).toList();
        return savedSlots;
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
