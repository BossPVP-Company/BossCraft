package com.bosspvp.core.skills.effects.types.chance;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.ChanceMultiplierEffect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EffectDontConsumeLapisChance extends ChanceMultiplierEffect {
    public EffectDontConsumeLapisChance(@NotNull BossPlugin plugin) {
        super(plugin, "dont_consume_lapis_chance");
    }
    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH)
    public void handle(EnchantItemEvent event){
        var player = event.getEnchanter();
        int cost = event.whichButton()+1;
        if(!passesChance(player)) return;
        getPlugin().getScheduler().runLater(2, ()-> {
            event.getInventory().addItem(new ItemStack(org.bukkit.Material.LAPIS_LAZULI, cost));
        });
    }
}
