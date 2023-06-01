package com.bosspvp.api.placeholders;

import com.bosspvp.api.BossPlugin;
import org.jetbrains.annotations.Nullable;

public interface InjectablePlaceholder extends Placeholder{
    /**
     * Get the plugin that holds the arguments.
     *
     * @return The plugin.
     */
    @Nullable
    @Override
    default BossPlugin getPlugin() {
        return null;
    }
}
