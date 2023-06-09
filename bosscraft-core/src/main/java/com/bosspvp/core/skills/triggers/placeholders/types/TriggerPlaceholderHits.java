package com.bosspvp.core.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.event.TriggerDispatchEvent;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TriggerPlaceholderHits extends TriggerPlaceholder {
    private final String HITS_META_KEY = "bosspvp_tracked_hits";
    public TriggerPlaceholderHits(@NotNull BossPlugin plugin) {
        super(plugin,"hits");
    }

    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        LivingEntity victim = data.victim();
        if(victim==null) return new ArrayList<>();
        Player player = data.player();
        if(player==null) return new ArrayList<>();


        return List.of(
                new StaticPlaceholder(
                        "hits",
                        ()->String.valueOf(getHits(victim,player))
                )
        );
    }


    //@TODO add trigger types TriggerMeleeAttack, TriggerBowAttack, TriggerTridentAttack
    //and then uncomment this
   /* @EventHandler
    public void trackHits(TriggerDispatchEvent event) {
        if (event.getTrigger().trigger() !in listOf(
                TriggerMeleeAttack,
                TriggerBowAttack,
                TriggerTridentAttack
        )
        ) {
            return
        }

        Player player = event.getTrigger().data().player();
        if(player == null) return;
        LivingEntity victim = event.getTrigger().data().victim();
        if(victim == null) return;

        val map = entity.getMetadata(HITS_META_KEY).firstOrNull()?.value() as? MutableMap<UUID, Int> ?: mutableMapOf()
        val hits = entity.getHits(player)
        if (entity.health >= entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value) {
            map[player.uniqueId] = 1
        } else {
            map[player.uniqueId] = hits + 1
        }

        entity.removeMetadata(HITS_META_KEY, plugin);
        entity.setMetadata(HITS_META_KEY, plugin.createMetadataValue(map));
    }*/
    private int getHits(@NotNull LivingEntity victim,
                        @NotNull Player player) {

        var metadata = victim.getMetadata(HITS_META_KEY);
        if(metadata.size()==0 || !(metadata.get(0) instanceof HashMap)){
            return 0;
        }
        var map = (HashMap<UUID,Integer>) metadata.get(0);

        return Objects.requireNonNullElse(map.get(player.getUniqueId()),0);
    }
}
