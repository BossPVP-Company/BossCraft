package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class TriggerBrew extends Trigger {
    public TriggerBrew(BossPlugin plugin){
        super(plugin, "brew", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.LOCATION,
                TriggerParameter.ITEM
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(BrewEvent event){
        var p = event.getContents().getViewers().get(0);
        if(!(p instanceof Player player)) return;
        ItemStack item = null;
        for(int i = 0; i < 3; i++){
            item = event.getContents().getItem(i);
            if(item != null&&item.getType()!= Material.AIR) break;
            item = null;
        }
        int amount = 0;
        for(int i = 0; i < 3; i++){
            ItemStack item1 = event.getContents().getItem(i);
            if(item1 != null && item1.getType()!= Material.AIR) {
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
