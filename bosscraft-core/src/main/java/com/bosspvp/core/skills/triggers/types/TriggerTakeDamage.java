package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;

public class TriggerTakeDamage extends Trigger {
    public TriggerTakeDamage(BossPlugin plugin) {
        super(plugin,"take_damage", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.EVENT
        ));
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(EntityDamageEvent event){
        var victim = event.getEntity();

        if(!(victim instanceof Player player)){
            return;
        }

        this.dispatch(
                player,
                TriggerData.builder()
                        .player(player)
                        .event(event)
                        .value(event.getFinalDamage())
                        .build()
        );
    }
}
