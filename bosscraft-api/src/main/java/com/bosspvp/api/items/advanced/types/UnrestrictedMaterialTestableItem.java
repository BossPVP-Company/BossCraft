package com.bosspvp.api.items.advanced.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnrestrictedMaterialTestableItem extends MaterialTestableItem {
    /**
     * Create a new simple recipe part.
     *
     * @param material The material.
     */
    public UnrestrictedMaterialTestableItem(@NotNull final Material material) {
        super(material);
    }

    /**
     * If the item matches the material.
     *
     * @param itemStack The item to test.
     * @return If the item is of the specified material.
     */
    @Override
    public boolean matches(@Nullable final ItemStack itemStack) {
        return itemStack != null && itemStack.getType() == this.getMaterial();
    }
}
