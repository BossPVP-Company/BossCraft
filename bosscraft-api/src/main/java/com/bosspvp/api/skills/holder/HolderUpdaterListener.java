package com.bosspvp.api.skills.holder;

import com.bosspvp.api.BossPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class HolderUpdaterListener implements Listener {
    private final BossPlugin plugin;

    private HolderManager holderManager;

    public HolderUpdaterListener(BossPlugin plugin) {
        this.plugin = plugin;
        this.holderManager = plugin.getSkillsManager().getHolderManager();
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        holderManager.refreshHolders(player);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getOnlinePlayers().forEach(it->holderManager.refreshHolders(it));
    }
    @EventHandler
    public void onInventoryDrop(PlayerDropItemEvent event) {
        holderManager.refreshHolders(event.getPlayer());
    }
    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent event) {
        holderManager.refreshHolders(event.getPlayer());
        plugin.getScheduler().run(()->holderManager.refreshHolders(event.getPlayer()));
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player)) return;
        holderManager.refreshHolders(player);

    }

    //@TODO not sure whether the similar event from paper can replace this (it is from eco)
    //so, I'll leave this here for now
    //later, I'll check when both events are called
     /*
    @EventHandler
    fun onArmorChange(event: ArmorChangeEvent) {
        event.player.refreshHolders()
    }
    */
}
