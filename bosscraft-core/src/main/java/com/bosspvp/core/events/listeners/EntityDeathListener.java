package com.bosspvp.core.events.listeners;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.events.death.EntityKilledByEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class EntityDeathListener implements Listener {
    private final BossPlugin plugin;
    private final HashMap<UUID,EntityKilledByEntityEvent.Builder> preparedEvents
            = new HashMap<>();
    public EntityDeathListener(BossPlugin plugin){
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }
        if(victim.getHealth() > event.getFinalDamage()) {
            return;
        }
        EntityKilledByEntityEvent.Builder builtEvent =
                EntityKilledByEntityEvent.builder()
                        .victim(victim);
        if(event.getDamager() instanceof Projectile projectile) {
            builtEvent.projectile(projectile);
            UUID shooterId = projectile.getOwnerUniqueId();
            if(shooterId != null) {
                Entity killer = Bukkit.getEntity(shooterId);
                if(killer != null) {
                    builtEvent.killer(killer);
                }else{
                    shooterId = null;
                }
            }
            if(shooterId==null){
                builtEvent.projectile(null);
                builtEvent.killer(projectile);
            }
        }
        preparedEvents.put(victim.getUniqueId(),builtEvent);
        plugin.getScheduler().runLater(5,()->{
            preparedEvents.remove(victim.getUniqueId());
        });

    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        var victim = event.getEntity();
        var drops = event.getDrops();
        var xp = event.getDroppedExp();
        EntityKilledByEntityEvent.Builder deathEvent =
                preparedEvents.get(victim.getUniqueId());
        if (deathEvent == null) {
            return;
        }
        preparedEvents.remove(victim.getUniqueId());
        deathEvent.drops(drops);
        deathEvent.experience(xp);
        deathEvent.deathEvent(event);
        plugin.getEventManager().callEvent(deathEvent.build());
    }
}
