package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;
import java.util.Set;

public class TriggerKill extends Trigger {
    public TriggerKill(BossPlugin plugin) {
        super(plugin,"kill", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.VICTIM,
                TriggerParameter.LOCATION,
                TriggerParameter.ITEM
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityDeathEvent event){
        var killer = event.getEntity().getKiller();
        var victim = event.getEntity();
        if(killer == null){
            return;
        }

        this.dispatch(
                killer,
                TriggerData.builder()
                        .player(killer)
                        .victim(victim)
                        .location(victim.getLocation())
                        .value(Objects.requireNonNull(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue())
                        .build()
        );
    }
    /*Trigger("kill") {
    override val parameters = setOf(
        TriggerParameter.PLAYER,
        TriggerParameter.VICTIM,
        TriggerParameter.LOCATION,
        TriggerParameter.ITEM
    )

    @EventHandler(ignoreCancelled = true)
    fun handle(event: EntityDeathByEntityEvent) {
        val killer = event.killer.tryAsPlayer() ?: return

        val victim = event.victim

        this.dispatch(
            killer,
            TriggerData(
                player = killer,
                victim = victim,
                location = victim.location,
                value = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            )
        )
    }*/
}
