package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerDamageItem extends Trigger {
    public TriggerDamageItem(@NotNull BossPlugin plugin) {
        super(plugin, "damage_item", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.LOCATION,
                TriggerParameter.ITEM,
                TriggerParameter.EVENT,
                TriggerParameter.VALUE
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(org.bukkit.event.player.PlayerItemDamageEvent event) {
        var player = event.getPlayer();
        this.dispatch(player, TriggerData.builder()
                .player(player)
                .location(player.getLocation())
                .item(event.getItem())
                .value(event.getDamage())
                .event(event)
                .build()
        );
    }
}
