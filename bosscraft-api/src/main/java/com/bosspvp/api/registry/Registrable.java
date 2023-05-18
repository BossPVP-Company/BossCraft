package com.bosspvp.api.registry;

import org.jetbrains.annotations.NotNull;

public interface Registrable {
    /**
     * Get the ID of the element.
     *
     * @return The ID.
     */
    @NotNull
    String getID();

    /**
     * Called after the element is registered in {@link Registry}
     */
    default void onRegister() {
        // override if needed
    }

    /**
     * Called before the element is removed from {@link Registry}
     */
    default void onRemove() {
        // override if needed
    }
}
