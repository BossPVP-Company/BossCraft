package com.bosspvp.core.skills.mutators.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.mutators.Mutator;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

public class MutatorLocationToVictim extends Mutator<Compilable.NoCompileData> {
    public MutatorLocationToVictim(@NotNull BossPlugin plugin) {
        super(plugin,"location_to_victim");
    }

    @Override
    protected TriggerData mutate(TriggerData data, Config config, NoCompileData compileData) {
        return data.copyToBuilder().location(data.victim() == null ? null : data.victim().getLocation()).build();
    }
}
