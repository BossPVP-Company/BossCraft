package com.bosspvp.api.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TriggerPlaceholderText extends TriggerPlaceholder {
    public TriggerPlaceholderText(@NotNull BossPlugin plugin) {
        super(plugin, "text");
    }

    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        String text = data.text();
        if(text==null) return new ArrayList<>();
        return List.of(
                new StaticPlaceholder("text", ()->text),
                new StaticPlaceholder("message", ()->text),
                new StaticPlaceholder("string", ()->text)
        );
    }
}
