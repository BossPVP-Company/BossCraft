package com.bosspvp.api.items.advanced.types;

import com.bosspvp.api.items.advanced.Items;
import com.bosspvp.api.items.advanced.TestableItem;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaterialTestableItem implements TestableItem {
    /**
     * The material.
     */
    private final Material material;

    /**
     * Create a new simple recipe part.
     *
     * @param material The material.
     */
    public MaterialTestableItem(@NotNull final Material material) {
        Validate.isTrue(material != Material.AIR, "You can't have air as the type!");

        this.material = material;
    }

    /**
     * If the item matches the material.
     *
     * @param itemStack The item to test.
     * @return If the item is of the specified material.
     */
    @Override
    public boolean matches(@Nullable final ItemStack itemStack) {
        boolean simpleMatches = itemStack != null && itemStack.getType() == material;

        if (!simpleMatches) {
            return false;
        }

        return !Items.isCustomItem(itemStack);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(material);
    }

    /**
     * Get the material.
     *
     * @return The material.
     */
    public Material getMaterial() {
        return this.material;
    }
}
