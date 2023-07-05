package com.bosspvp.core.skills.effects.types.multiplier;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.effects.templates.MultiplierEffect;
import com.bosspvp.api.utils.PotionUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EffectPotionDurationMultiplier extends MultiplierEffect {
    private static final Set<PotionType> cannotExtend = Set.of(
            PotionType.INSTANT_DAMAGE, PotionType.INSTANT_HEAL, PotionType.AWKWARD,
            PotionType.MUNDANE, PotionType.THICK, PotionType.WATER
    );

    public EffectPotionDurationMultiplier(BossPlugin plugin) {
        super(plugin, "potion_duration_multiplier");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handle(BrewEvent event) {
        Player player = event.getContents().getViewers().stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .findFirst()
                .orElse(null);
        if (player == null) {
            return;
        }
        double multiplier = getMultiplier(player);
        getPlugin().getScheduler().run(() -> {
            for (int i = 0; i <= 2; i++) {
                ItemStack item = event.getContents().getItem(i);
                if (item == null) {
                    continue;
                }
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                PotionData potionData = meta.getBasePotionData();
                if (cannotExtend.contains(potionData.getType())) {
                    continue;
                }
                int duration = PotionUtils.getDuration(potionData);
                int delta = (int) (duration * multiplier) - duration;

                meta.getPersistentDataContainer().set(
                        new NamespacedKey(getPlugin(), "duration-delta"),
                        PersistentDataType.INTEGER,
                        delta
                );
                item.setItemMeta(meta);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handlePotionDelta(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null) {
            return;
        }
        Integer delta = meta.getPersistentDataContainer().get(
                new NamespacedKey(getPlugin(), "duration-delta"),
                PersistentDataType.INTEGER
        );
        if (delta == null) {
            return;
        }
        PotionData potionData = meta.getBasePotionData();
        HashMap<PotionEffectType, Integer> effects = new HashMap<>();

        if (potionData.getType() == PotionType.TURTLE_MASTER) {
            effects.put(PotionEffectType.SLOW, 4);
            effects.put(PotionEffectType.DAMAGE_RESISTANCE, 2);
        } else {
            PotionEffectType effectType = potionData.getType().getEffectType();
            if (effectType == null) {
                return;
            }
            effects.put(effectType, potionData.isUpgraded() ? 2 : 1);
        }
        int newDuration = PotionUtils.getDuration(potionData) + delta;

        for (Map.Entry<PotionEffectType, Integer> entry : effects.entrySet()) {
            player.addPotionEffect(
                    new PotionEffect(
                            entry.getKey(),
                            newDuration,
                            entry.getValue() - 1
                    )
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handlePotionData(PotionSplashEvent event){
        var entities = event.getAffectedEntities();
        var item = event.getPotion().getItem();
        var meta = (PotionMeta) item.getItemMeta();
        if (meta == null) {
            return;
        }
        PotionData potionData = meta.getBasePotionData();
        HashMap<PotionEffectType,Integer> effects = new HashMap<>();
        if(potionData.getType() == PotionType.TURTLE_MASTER){
            effects.put(PotionEffectType.SLOW,4);
            effects.put(PotionEffectType.DAMAGE_RESISTANCE,2);
        }else {
            PotionEffectType effectType = potionData.getType().getEffectType();
            if (effectType == null) {
                return;
            }
            effects.put(effectType, potionData.isUpgraded() ? 2 : 1);
        }
        int delta =  meta.getPersistentDataContainer().get(
                new NamespacedKey(getPlugin(), "duration-delta"),
                PersistentDataType.INTEGER
        );
        for(LivingEntity entity : entities){
            int newDuration =(int) (( PotionUtils.getDuration(potionData) + delta ) * event.getIntensity(entity));
            for(Map.Entry<PotionEffectType,Integer> entry : effects.entrySet()){
                entity.addPotionEffect(
                        new PotionEffect(
                                entry.getKey(),
                                newDuration,
                                entry.getValue() - 1
                        )
                );
            }
        }
    }
}
