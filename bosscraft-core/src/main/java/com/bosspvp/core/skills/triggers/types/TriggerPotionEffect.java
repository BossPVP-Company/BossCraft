package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerPotionEffect extends Trigger {
    public TriggerPotionEffect(@NotNull BossPlugin plugin) {
        super(plugin, "potion_effect", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.LOCATION,
                TriggerParameter.EVENT
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityPotionEffectEvent event){
        if(event.getNewEffect() == null) return;
        if(!(event.getEntity() instanceof Player player)) return;
        this.dispatch(player,
                TriggerData.builder()
                .player(player)
                .location(player.getLocation())
                .event(event)
                .text(event.getNewEffect().getType().getName().toLowerCase())
                .build());
    }
}
