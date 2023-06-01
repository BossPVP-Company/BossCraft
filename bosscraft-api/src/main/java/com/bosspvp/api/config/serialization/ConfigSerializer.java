package com.bosspvp.api.config.serialization;

import com.bosspvp.api.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Save objects to configs.
 *
 * @param <T> The type of object to save
 */
public interface ConfigSerializer<T> {

    /**
     * Save an object to a config.
     *
     * @param obj The object.
     * @return The config.
     */
    @NotNull
    Config serializeToConfig(@NotNull T obj);
}
