package com.bosspvp.api.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerMove extends Trigger {
    public TriggerMove(@NotNull BossPlugin plugin) {
        super(plugin,"move",
                Set.of(
                        TriggerParameter.PLAYER,
                        TriggerParameter.LOCATION,
                        TriggerParameter.VELOCITY,
                        TriggerParameter.EVENT,
                        TriggerParameter.ITEM
                )
        );
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(PlayerMoveEvent event){
        var player = event.getPlayer();

        //paper
        if (!event.hasChangedBlock()) {
            return;
        }
        var distance = event.getTo().getWorld()==event.getFrom().getWorld() ?
                event.getTo().distance(event.getFrom()) : 0.0;
        this.dispatch(
                player,
                TriggerData.builder()
                        .player(player)
                        .location(player.getLocation())
                        .velocity(player.getVelocity())
                        .event(event)
                        .item(player.getInventory().getItemInMainHand())
                        .value(distance)
                        .build()
        );
    }

}
