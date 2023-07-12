package com.bosspvp.api.gui.components;

import com.bosspvp.api.gui.slot.GuiSlot;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A component for level rewards
 * Use {@link #initialize(List, int)} inside the constructor!
 * I separated that method from the constructor to allow the
 * child class to set its variables affecting on abstract classes
 */
public abstract class LevelComponent extends GuiPage {
    private char[] progressionOrder = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();


    private HashMap<Integer, HashMap<Integer, GuiSlot>> slots = new HashMap<>();
    private int levelsPerPage;
    private int pages;

    public LevelComponent(List<String> pattern,
                          int maxLevel) {
        super(pattern.get(0).toCharArray().length, pattern.size(),-1);

    }

    protected void initialize(List<String> pattern, int maxLevel){
        var progressionSlots = new HashMap<Integer, Integer>();

        var x = 0;
        for (String row : pattern) {
            var y = 0;
            for (char element : row.toCharArray()) {
                if (element == '0') {
                    continue;
                }

                var pos = -1;
                for (int i = 0; i < progressionOrder.length;i++) {
                    if (element == progressionOrder[i]) {
                        pos = i;
                        break;
                    }
                }
                if (pos == -1) {
                    continue;
                }

                progressionSlots.put(pos+1,x*row.toCharArray().length + y);
                y++;
            }
            x++;
        }

        levelsPerPage = progressionSlots.size();
        pages = (int) Math.ceil((double) maxLevel / levelsPerPage);
        setMaxPage(pages-1);

        for (int page = 0; page < pages; page++) {
            for (Map.Entry<Integer,Integer> entry : progressionSlots.entrySet()) {
                var level = (page * levelsPerPage) + entry.getKey();

                if (level > maxLevel) {
                    continue;
                }

                var pageSlots = slots.get(page);
                if(pageSlots == null){
                    pageSlots = new HashMap<>();
                }

                pageSlots.put(
                        entry.getValue(),
                        GuiSlot.builder(entry.getValue())
                                .setItem(getLevelItem(level, getLevelState(level)))
                                .build()
                );

                slots.put(page,pageSlots);
            }
        }
    }

    @Override
    public @Nullable GuiSlot getSlotAt(int row, int column) {
        var page = slots.get(getCurrentPage());
        if(page==null) return null;
        return page.get(row * getColumnsSize() + column);
    }

    //not needed here
    @Override
    List<GuiSlot> getElements(int page) {
        return null;
    }

    public abstract ItemStack getLevelItem(int level, LevelState levelState);

    public abstract LevelState getLevelState(int level);




    public enum LevelState{
        UNLOCKED("unlocked"),
        IN_PROGRESS("in-progress"),
        LOCKED("locked");

        @Getter
        private final String key;
        LevelState(@NotNull String key){
            this.key = key;
        }

    }
}
