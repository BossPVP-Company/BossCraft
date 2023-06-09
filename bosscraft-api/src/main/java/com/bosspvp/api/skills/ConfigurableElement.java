package com.bosspvp.api.skills;

import com.bosspvp.api.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ConfigurableElement {


    /**
     * The uuid of the effect.
     */
    @NotNull UUID getUUID();

    /**
     * The config of the effect.
     */
    @NotNull Config getConfig();
}
