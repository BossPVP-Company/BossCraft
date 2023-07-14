package com.bosspvp.api.items.advanced.types;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HashedItem {
    /**
     * The item.
     */
    private final ItemStack item;

    /**
     * The hash.
     */
    private final int hash;

    /**
     * Create new hashed item.
     *
     * @param item The item.
     * @param hash The hash.
     */
    private HashedItem(@NotNull final ItemStack item,
                       final int hash) {
        this.item = item;
        this.hash = hash;
    }

    /**
     * Get the item.
     *
     * @return The ItemStack.
     */
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Get the hash.
     *
     * @return The hash.
     */
    public int getHash() {
        return this.hash;
    }

    /**
     * Kotlin destructuring support.
     *
     * @return The ItemStack.
     */
    public ItemStack component1() {
        return this.getItem();
    }

    /**
     * Kotlin destructuring support.
     *
     * @return The hash.
     */
    public int component2() {
        return this.getHash();
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (!(other instanceof HashedItem o)) {
            return false;
        }

        return o.hash == this.hash;
    }

    /**
     * Hashed item from an item.
     *
     * @param item The item.
     * @return The hashed item.
     */
    public static HashedItem of(@NotNull final ItemStack item) {
        return new HashedItem(item, item.hashCode());
    }
}
