package com.bosspvp.api.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerMeleeAttack extends Trigger {
    public TriggerMeleeAttack(@NotNull BossPlugin plugin) {
        super(plugin, "melee_attack", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.VICTIM,
                TriggerParameter.LOCATION,
                TriggerParameter.EVENT,
                TriggerParameter.ITEM
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityDamageByEntityEvent event){
       if(!(event.getDamager() instanceof Player player)) return;
       if(!(event.getEntity() instanceof Player victim)) return;
       if(event.isCancelled()) return;
       if(event.getCause() == EntityDamageByEntityEvent.DamageCause.THORNS) return;
       this.dispatch(
                    player,
                    TriggerData.builder()
                            .player(player)
                            .victim(victim)
                            .location(victim.getLocation())
                            .event(event)
                            .item(player.getInventory().getItemInMainHand())
                            .value(event.getFinalDamage())
                            .build()
            );

    }
}
