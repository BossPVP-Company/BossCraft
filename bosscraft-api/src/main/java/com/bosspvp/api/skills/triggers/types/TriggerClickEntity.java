package com.bosspvp.api.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerClickEntity extends Trigger {
    public TriggerClickEntity(@NotNull BossPlugin plugin) {
        super(plugin, "click_entity",
                Set.of(
                        TriggerParameter.PLAYER,
                        TriggerParameter.LOCATION,
                        TriggerParameter.VICTIM,
                        TriggerParameter.EVENT
                ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(PlayerInteractEntityEvent event){
        var player = event.getPlayer();
        var entity = event.getRightClicked();
        this.dispatch(
                player,
                TriggerData.builder()
                        .player(player)
                        .event(event)
                        .location(entity.getLocation())
                        .victim((LivingEntity) entity)
                        .build()
        );
    }
}
