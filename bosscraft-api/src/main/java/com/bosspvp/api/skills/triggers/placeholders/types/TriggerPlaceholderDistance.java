package com.bosspvp.api.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TriggerPlaceholderDistance extends TriggerPlaceholder {
    public TriggerPlaceholderDistance(@NotNull BossPlugin plugin) {
        super(plugin,"distance");
    }

    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        var victim = data.victim();
        if(victim==null) return new ArrayList<>();
        var player = data.player();
        if(player==null) return new ArrayList<>();

        return List.of(
                new StaticPlaceholder("distance",
                        ()-> String.valueOf(player.getLocation().toVector().distance(victim.getLocation().toVector())))
        );
    }
}
