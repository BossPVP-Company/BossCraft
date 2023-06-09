package com.bosspvp.api.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerConsume extends Trigger {
    public TriggerConsume(@NotNull BossPlugin plugin) {
        super(plugin, "consume",
                Set.of(
                        TriggerParameter.PLAYER,
                        TriggerParameter.ITEM,
                        TriggerParameter.EVENT
                ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(PlayerItemConsumeEvent event){
        var player = event.getPlayer();
        this.dispatch(
                player,
                TriggerData.builder()
                        .player(player)
                        .event(event)
                        .item(event.getItem())
                        .build()
        );
    }
}
