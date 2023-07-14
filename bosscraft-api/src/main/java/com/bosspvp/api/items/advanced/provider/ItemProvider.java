package com.bosspvp.api.items.advanced.provider;

import com.bosspvp.api.items.advanced.TestableItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ItemProvider {
    /**
     * The namespace.
     */
    private final String namespace;

    /**
     * Create a new ItemProvider for a specific namespace.
     *
     * @param namespace The namespace.
     */
    protected ItemProvider(@NotNull final String namespace) {
        this.namespace = namespace;
    }

    /**
     * Provide a TestableItem for a given key.
     *
     * @param key The item ID.
     * @return The TestableItem, or null if not found.
     */
    @Nullable
    public abstract TestableItem provideForKey(@NotNull String key);

    /**
     * Get the namespace.
     *
     * @return The namespace.
     */
    public String getNamespace() {
        return this.namespace;
    }
}
