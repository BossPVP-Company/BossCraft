package com.bosspvp.api.registry;

import org.jetbrains.annotations.NotNull;

/**
 * Registrable
 * <p></p>
 * Implement it for a class
 * which you want to use in {@link Registry}
 * <p></p>
 * Registrable class has an ID.
 * @see Registrable#onRegister()
 * @see Registrable#onRemove()
 */
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
