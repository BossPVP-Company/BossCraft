package com.bosspvp.core.skills.effects.types.multiplier;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.MultiplierEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class EffectXpMultiplier extends MultiplierEffect {
    public EffectXpMultiplier(BossPlugin plugin) {
        super(plugin,"xp_multiplier");
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(PlayerExpChangeEvent event) {
        var player = event.getPlayer();
        var multiplier = getMultiplier(player);
        event.setAmount((int) Math.ceil(event.getAmount() * multiplier));
    }
}
