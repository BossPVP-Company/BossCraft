package com.bosspvp.test.config;

import com.bosspvp.api.config.impl.BossConfigOkaeri;
import com.bosspvp.api.config.impl.ConfigSettings;
import org.jetbrains.annotations.NotNull;

public class ConfigFileOkaeri extends BossConfigOkaeri {

    //have to be added, otherwise API gonna work incorrectly
    private ConfigSettings settings = new ConfigSettings();

    private String test = "Teeeest";


    @NotNull
    public ConfigSettings getSettings() {
        if(settings==null) return new ConfigSettings();
        return settings;
    }
}
