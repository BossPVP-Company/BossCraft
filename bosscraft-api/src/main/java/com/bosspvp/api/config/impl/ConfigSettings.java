package com.bosspvp.api.config.impl;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import lombok.Getter;

@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class ConfigSettings extends BossConfigOkaeri {
    @Comment({"Plugin debug mode"})
    @Getter
    private boolean debug = false;


}
