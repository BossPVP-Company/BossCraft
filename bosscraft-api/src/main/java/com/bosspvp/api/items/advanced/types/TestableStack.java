package com.bosspvp.api.items.advanced.types;

import com.bosspvp.api.items.advanced.TestableItem;
import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestableStack implements TestableItem{
    /**
     * The item.
     */
    private final TestableItem handle;

    /**
     * The amount.
     */
    private final int amount;

    /**
     * Create a new testable stack.
     *
     * @param item   The item.
     * @param amount The amount.
     */
    public TestableStack(@NotNull final TestableItem item,
                         final int amount) {
        Validate.isTrue(!(item instanceof TestableStack), "You can't stack a stack!");
        Validate.isTrue(!(item instanceof EmptyTestableItem), "You can't stack air!");

        this.handle = item;
        this.amount = amount;
    }

    /**
     * If the item matches the material.
     *
     * @param itemStack The item to test.
     * @return If the item is of the specified material.
     */
    @Override
    public boolean matches(@Nullable final ItemStack itemStack) {
        return itemStack != null && handle.matches(itemStack) && itemStack.getAmount() >= amount;
    }

    @Override
    public ItemStack getItem() {
        ItemStack temp = handle.getItem().clone();
        temp.setAmount(amount);
        return temp;
    }

    /**
     * Get the handle.
     *
     * @return The handle.
     */
    public TestableItem getHandle() {
        return this.handle;
    }

    /**
     * Get the amount in the stack.
     *
     * @return The amount.
     */
    public int getAmount() {
        return this.amount;
    }
}
