package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectDamageTwice extends Effect<Compilable.NoCompileData> {
    private final String META_KEY = "bosscore-damaged-twice";
    public EffectDamageTwice(@NotNull BossPlugin plugin) {
        super(plugin, "damage_twice");
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        if(!(data.event() instanceof EntityDamageByEntityEvent event)) {
            return false;
        }
        if(data.victim() == null) {
            return false;
        }
        if(data.victim().hasMetadata(META_KEY)) {
            return false;
        }
        getPlugin().getScheduler().run(() -> {
            data.victim().setMetadata(META_KEY, new FixedMetadataValue(getPlugin(), true));
            data.victim().setNoDamageTicks(0);
            data.victim().damage(event.getDamage(), event.getDamager());
        });
        return true;
    }
    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.VICTIM, TriggerParameter.EVENT);
    }

    @Override
    public boolean isSupportsDelay() {
        return false;
    }
}
