package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectIgnite extends Effect<Compilable.NoCompileData> {

    public EffectIgnite(@NotNull BossPlugin plugin) {
        super(plugin,"ignite");
        setArguments(it->{
            it.require("damage_per_tick", "You must specify the damage per fire tick!");
            it.require("ticks", "You must specify the duration!");
        });
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, Compilable.NoCompileData compileData) {
        LivingEntity victim = data.victim();
        if(victim==null) return false;
        double damage = config.getEvaluated("damage_per_tick", data.toPlaceholderContext(config));
        int duration = (int) config.getEvaluated("ticks", data.toPlaceholderContext(config));

        victim.setFireTicks(duration);
        victim.setMetadata("bosspvp-ignite",
                new FixedMetadataValue(
                        getPlugin(),
                        damage
                )
        );

        return true;
    }
    @EventHandler
    public void onBurn(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) {
            return;
        }
        if (!event.getEntity().hasMetadata("bosspvp-ignite")) {
            return;
        }

        var meta = event.getEntity().getMetadata("bosspvp-ignite");
        if(meta.size()==0) return;
        event.setDamage(meta.get(0).asDouble());
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(
                TriggerParameter.VICTIM,
                TriggerParameter.PLAYER
        );
    }
    /*

    @EventHandler
    fun onBurn(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.FIRE_TICK) {
            return
        }
        if (!event.entity.hasMetadata("libreforge-ignite")) {
            return
        }

        event.damage = event.entity.getMetadata("libreforge-ignite")[0].asDouble()
    }*/
}
