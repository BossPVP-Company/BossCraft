package com.bosspvp.core.skills.mutators.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.mutators.Mutator;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MutatorVictimAsPlayer extends Mutator<Compilable.NoCompileData> {
    public MutatorVictimAsPlayer(@NotNull BossPlugin plugin) {
        super(plugin,"victim_as_player");
    }

    @Override
    protected TriggerData mutate(TriggerData data, Config config, NoCompileData compileData) {
        return data.copyToBuilder().player((data.victim() instanceof Player p) ? p : null).build();
    }
}
