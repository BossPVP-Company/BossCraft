package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.Set;

public class TriggerTridentAttack extends Trigger {

    private final String META_KEY = "bosscore_trident_holders";
    public TriggerTridentAttack(BossPlugin plugin) {
        super(plugin,"trident_attack", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.VICTIM,
                TriggerParameter.LOCATION,
                TriggerParameter.PROJECTILE,
                TriggerParameter.EVENT,
                TriggerParameter.ITEM,
                TriggerParameter.VELOCITY
        ));
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        var shooter = event.getEntity().getShooter();
        if(!(shooter instanceof Player)) return;
        var trident = event.getEntity();
        if(!(trident instanceof Trident)) return;
        trident.setMetadata(
                META_KEY,
                new FixedMetadataValue(plugin,plugin.getSkillsManager().getHolderManager().getPlayerHolders((Player) shooter))
        );
    }

    @EventHandler(ignoreCancelled = true)
    public void handle(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Trident trident)) return;
        if(!(event.getEntity() instanceof Player victim)) return;
        if(!(trident.getShooter() instanceof Player shooter)) return;
        if(event.isCancelled()) return;
        this.dispatch(
                shooter,
                TriggerData.builder()
                        .player(shooter)
                        .victim(victim)
                        .location(victim.getLocation())
                        .event(event)
                        .velocity(trident.getVelocity())
                        .projectile(trident)
                        .item(trident.getItem())
                        .value(event.getFinalDamage())
                        .build(),
                (List<ProvidedHolder>) trident.getMetadata(META_KEY).get(0).value()
        );
    }
}
