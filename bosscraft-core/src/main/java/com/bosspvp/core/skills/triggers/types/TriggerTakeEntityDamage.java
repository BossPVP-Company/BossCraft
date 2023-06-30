package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerTakeEntityDamage extends Trigger {
    public TriggerTakeEntityDamage(@NotNull BossPlugin plugin) {
        super(plugin, "take_entity_damage", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.VICTIM,
                TriggerParameter.LOCATION,
                TriggerParameter.EVENT
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof LivingEntity attacker)){
            return;
        }
        var victim = event.getEntity();
        if(!(victim instanceof Player)){
            return;
        }
        if(event.isCancelled()){
            return;
        }
        if(event.getCause() == EntityDamageEvent.DamageCause.THORNS){
            return;
        }
        this.dispatch(
                (Player) victim,
                TriggerData.builder()
                        .player((Player) victim)
                        .victim(attacker)
                        .location(attacker.getLocation())
                        .event(event)
                        .value(event.getFinalDamage())
                        .build()
        );
    }
}
