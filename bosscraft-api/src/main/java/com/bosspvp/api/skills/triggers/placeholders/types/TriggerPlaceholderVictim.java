package com.bosspvp.api.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TriggerPlaceholderVictim extends TriggerPlaceholder {
    public TriggerPlaceholderVictim(@NotNull BossPlugin plugin) {
        super(plugin,"victim");
    }


    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        LivingEntity victim = data.victim();
        if(victim==null) return new ArrayList<>();
        return List.of(
                new StaticPlaceholder("victim_health",()->String.valueOf(victim.getHealth())),
                new StaticPlaceholder("victim_max_health",()->
                        String.valueOf(
                                victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)!=null ?
                                victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() : 0.0
                        )
                )
        );
    }
}
