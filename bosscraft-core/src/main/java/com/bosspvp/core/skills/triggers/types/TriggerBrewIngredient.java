package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;

import java.util.Set;

public class TriggerBrewIngredient extends Trigger {
    public TriggerBrewIngredient(BossPlugin plugin) {
        super(plugin,"brew_ingredient",
                Set.of(
                        TriggerParameter.PLAYER,
                        TriggerParameter.LOCATION,
                        TriggerParameter.ITEM
                )
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(BrewEvent event){
        var p = event.getContents().getViewers().get(0);
        if(!(p instanceof Player player)) return;
        var item = event.getContents().getIngredient();
        var amount = 0;
        for(int i = 0; i < 3; i++){
            var item1 = event.getContents().getItem(i);
            if(item1 != null && item1.getType()!= org.bukkit.Material.AIR) {
                amount++;
            }
        }
        this.dispatch(
                player,
                TriggerData.builder()
                        .player(player)
                        .item(item)
                        .value(amount)
                        .build()
        );
    }
}
