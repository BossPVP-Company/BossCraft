package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Set;

public class TriggerBowAttack extends Trigger {

    public TriggerBowAttack(BossPlugin plugin) {
        super(plugin,"bow_attack", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.VICTIM,
                TriggerParameter.LOCATION,
                TriggerParameter.EVENT,
                TriggerParameter.VELOCITY,
                TriggerParameter.PROJECTILE
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Arrow arrow)) return;
        if(!(event.getEntity() instanceof Player victim)) return;
        if(!(arrow.getShooter() instanceof Player shooter)) return;
        if(event.isCancelled()) return;
        this.dispatch(
                shooter,
                TriggerData.builder()
                        .player(shooter)
                        .victim(victim)
                        .location(victim.getLocation())
                        .event(event)
                        .velocity(arrow.getVelocity())
                        .projectile(arrow)
                        .value(event.getFinalDamage())
                        .build()
        );
    }
}
