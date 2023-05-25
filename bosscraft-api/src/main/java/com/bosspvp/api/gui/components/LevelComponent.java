package com.bosspvp.api.gui.components;

import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.slot.GuiSlot;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public abstract class LevelComponent implements GuiComponent {
    private char[] progressionOrder = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

/*
    private HashMap<Integer, HashMap<Integer, GuiSlot>> slots = new HashMap<>();

    val levelsPerPage: Int
    val pages: Int

    public LevelComponent(List<String> pattern,
                          int maxLevel) {
        val progressionSlots = mutableMapOf<Int, GUIPosition>()

        var x = 0
        for (row in pattern) {
            x++
            var y = 0
            for (char in row) {
                y++
                if (char == '0') {
                    continue
                }

                val pos = progressionOrder.indexOf(char)

                if (pos == -1) {
                    continue
                }

                progressionSlots[pos + 1] = GUIPosition(x, y)
            }
        }

        levelsPerPage = progressionSlots.size
        pages = ceil(maxLevel.toDouble() / levelsPerPage).toInt()

        for (page in 1..pages) {
            for ((levelOffset, position) in progressionSlots) {
                val level = ((page - 1) * levelsPerPage) + levelOffset

                if (level > maxLevel) {
                    continue
                }

                val pageSlots = slots[page] ?: mutableMapOf()

                pageSlots[position] = slot { player, menu ->
                        getLevelItem(
                                player,
                                menu,
                                level,
                                getLevelState(
                                        player,
                                        level
                                )
                        )
                }

                slots[page] = pageSlots
            }
        }
    }

    @Override
    public @Nullable GuiSlot getSlotAt(int row, int column) {
        return slots[menu.getPage(player)]?.get(GUIPosition(row, column))
    }


    abstract ItemStack getLevelItem(int level, LevelState levelState);

    abstract LevelState getLevelState(int level);




    public enum LevelState{
        UNLOCKED("unlocked"),
        IN_PROGRESS("in-progress"),
        LOCKED("locked");

        @Getter
        private final String key;
        LevelState(@NotNull String key){
            this.key = key;
        }

    }*/
}
