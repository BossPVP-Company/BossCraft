package com.bosspvp.api.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TriggerPlaceholderDistance extends TriggerPlaceholder {
    public TriggerPlaceholderDistance(@NotNull BossPlugin plugin) {
        super(plugin,"distance");
    }

    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        return null;
    }
}
