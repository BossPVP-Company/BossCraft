package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Set;

public class TriggerEnchantItem extends Trigger {
    public TriggerEnchantItem(BossPlugin plugin) {
        super(plugin,"enchant_item", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.LOCATION,
                TriggerParameter.ITEM
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EnchantItemEvent event){
        var player = event.getEnchanter();
        var item = event.getItem();

        this.dispatch(
                player,
                TriggerData.builder()
                        .player(player)
                        .item(item)
                        .value(event.getExpLevelCost())
                        .build()
        );
    }
}
