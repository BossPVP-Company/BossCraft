package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.MultiplierEffect;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EffectBrewTimeMultiplier extends MultiplierEffect {
    public EffectBrewTimeMultiplier(BossPlugin plugin) {
        super(plugin, "brew_time_multiplier");
    }
    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH, ignoreCancelled = true)
    public void handle(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player player)) return;
        var multiplier = getMultiplier(player);
        getPlugin().getScheduler().runLater(2,()->{
            if(player.getOpenInventory().getTopInventory().getHolder() instanceof BrewingStand stand){
                if(stand.getBrewingTime() == 400){
                    stand.setBrewingTime((int) (stand.getBrewingTime() * multiplier));
                    stand.update();
                    player.updateInventory();
                }
            }
        });

    }
}
