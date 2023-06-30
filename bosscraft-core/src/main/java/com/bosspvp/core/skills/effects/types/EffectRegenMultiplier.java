package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.MultiplierEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EffectRegenMultiplier extends MultiplierEffect {
    public EffectRegenMultiplier(BossPlugin plugin) {
        super(plugin, "regen_multiplier");
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityRegainHealthEvent event){
        var player = event.getEntity();
        if(!(player instanceof Player)){
            return;
        }
        event.setAmount(event.getAmount() * getMultiplier((Player) player));
    }
}
