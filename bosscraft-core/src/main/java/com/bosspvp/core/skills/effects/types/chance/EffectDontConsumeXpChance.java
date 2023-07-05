package com.bosspvp.core.skills.effects.types.chance;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.ChanceMultiplierEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.jetbrains.annotations.NotNull;

public class EffectDontConsumeXpChance extends ChanceMultiplierEffect {
    public EffectDontConsumeXpChance(@NotNull BossPlugin plugin) {
        super(plugin, "dont_consume_xp_chance");
    }
    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH, ignoreCancelled = true)
    public void handle(EnchantItemEvent event){
        var player = event.getEnchanter();
        var cost = event.whichButton() + 1;
        if(!passesChance(player)) return;
        getPlugin().getScheduler().runLater(2, ()->{
            player.giveExpLevels(cost);
        });
    }
}
