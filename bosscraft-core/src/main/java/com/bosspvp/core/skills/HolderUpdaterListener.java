package com.bosspvp.core.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.holder.HolderManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Collectors;

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void clearOnQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for(var entry : holderManager.getProvidedActiveEffects(player)) {
            for(var effect : entry.effects()) {
                effect.disable(player,entry.holder(),false);
            }
        }

        // Extra fix for pre-4.2.3
        fixAttributes(player);

        holderManager.updateHolders(player);
        holderManager.purgePreviousHolders(player);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void scanOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        fixAttributes(player);
        holderManager.updateHolders(player);
        plugin.getScheduler().run(()->holderManager.updateEffects(player));
    }

    private void fixAttributes(Player player){
        for(Attribute attribute : Attribute.values()){
            var inst = player.getAttribute(attribute);
            if(inst==null) continue;
            var mods = inst.getModifiers().stream().filter(it->it.getName().startsWith("bosspvp")).toList();
            for(var mod : mods){
                inst.removeModifier(mod);
            }
        }
        if(player.getHealth()>player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()){
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        }
        for(var effect : plugin.getSkillsManager().getEffectsRegistry().getRegistry().values()){
            for(Attribute attribute : Attribute.values()){
                var inst = player.getAttribute(attribute);
                if(inst==null) continue;
                var mods = inst.getModifiers().stream()
                        .filter(it->it.getName().startsWith(effect.getId())).toList();
                for(var mod : mods){
                    inst.removeModifier(mod);
                }
            }
        }
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
