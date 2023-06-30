package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectCancelEvent extends Effect<Compilable.NoCompileData> {


    public EffectCancelEvent(@NotNull BossPlugin plugin) {
        super(plugin, "cancel_event");

    }
    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {

        if(data.event()==null) {
            return false;
        }
        if (data.event() instanceof Cancellable event) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSupportsDelay() {
        return false;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.EVENT);
    }
}
