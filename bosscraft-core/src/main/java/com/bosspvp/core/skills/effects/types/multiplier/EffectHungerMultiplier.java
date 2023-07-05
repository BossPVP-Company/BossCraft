package com.bosspvp.core.skills.effects.types.multiplier;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.MultiplierEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EffectHungerMultiplier extends MultiplierEffect {
    public EffectHungerMultiplier(BossPlugin plugin) {
        super(plugin, "hunger_multiplier");
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(FoodLevelChangeEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        var diff = event.getFoodLevel() - player.getFoodLevel();
        if(diff >= 0) return;
        var multiplier = getMultiplier(player);
        if(multiplier < 1){
            if(Math.random() > multiplier){
                event.setCancelled(true);
            }
        }else{
            event.setFoodLevel(player.getFoodLevel() + (int) Math.ceil(diff * multiplier));
        }
    }

}
