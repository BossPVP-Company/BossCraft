package com.bosspvp.core.skills.mutators.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.mutators.Mutator;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

public class MutatorPlayerAsVictim extends Mutator<Compilable.NoCompileData> {
    public MutatorPlayerAsVictim(@NotNull BossPlugin plugin) {
        super(plugin,"player_as_victim");
    }

    @Override
    protected TriggerData mutate(TriggerData data, Config config, NoCompileData compileData) {
        return data.copyToBuilder().victim(data.player()).build();
    }
}
