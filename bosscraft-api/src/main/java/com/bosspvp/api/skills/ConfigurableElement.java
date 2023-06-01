package com.bosspvp.api.skills;

import com.bosspvp.api.config.Config;

import java.util.UUID;

public interface ConfigurableElement {


    /**
     * The uuid of the effect.
     */
    UUID getUUID();

    /**
     * The config of the effect.
     */
    Config getConfig();
}
