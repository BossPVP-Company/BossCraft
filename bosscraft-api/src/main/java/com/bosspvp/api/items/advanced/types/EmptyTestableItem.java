package com.bosspvp.api.items.advanced.types;

import com.bosspvp.api.items.advanced.TestableItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EmptyTestableItem implements TestableItem {
    /**
     * Create a new empty recipe part.
     */
    public EmptyTestableItem() {

    }

    /**
     * If the item is empty.
     *
     * @param itemStack The item to test.
     * @return If the item is empty.
     */
    @Override
    public boolean matches(@Nullable final ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.AIR);
    }

}