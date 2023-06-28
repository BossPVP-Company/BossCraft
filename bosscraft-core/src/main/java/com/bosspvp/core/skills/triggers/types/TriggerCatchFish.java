package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Objects;
import java.util.Set;

public class TriggerCatchFish  extends Trigger{
    public TriggerCatchFish(BossPlugin plugin) {
        super(plugin,"catch_fish", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.LOCATION,
                TriggerParameter.EVENT,
                TriggerParameter.ITEM
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(PlayerFishEvent event){
        if(event.getState() != PlayerFishEvent.State.CAUGHT_FISH){
            return;
        }
        this.dispatch(
                event.getPlayer(),
                TriggerData.builder()
                        .player(event.getPlayer())
                        .location(event.getHook().getLocation())
                        .event(event)
                        .item(((Item) Objects.requireNonNull(event.getCaught())).getItemStack())
                        .value(event.getExpToDrop())
                        .build()
        );
    }
}
