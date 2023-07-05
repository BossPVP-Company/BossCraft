package com.bosspvp.core.skills.effects.types.multiplier;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.MultiplierEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class EffectReelSpeedMultiplier extends MultiplierEffect {
    public EffectReelSpeedMultiplier(BossPlugin plugin) {
        super(plugin, "reel_speed_multiplier");
    }
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(PlayerFishEvent event) {
        if (!(event.getState() == PlayerFishEvent.State.CAUGHT_FISH ||
                event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY)) {
            return;
        }
        if(event.getCaught()==null) {
            return;
        }
        Player player = event.getPlayer();
        double multiplier = getMultiplier(player);
        if (multiplier == 1.0) {
            return;
        }
        Vector vector = player.getEyeLocation().toVector()
            .clone()
            .subtract(event.getCaught().getLocation().toVector())
            .normalize()
            .multiply(multiplier);
        getPlugin().getScheduler().run(() -> {
            event.getCaught().setVelocity(vector);
        });
    }
}
