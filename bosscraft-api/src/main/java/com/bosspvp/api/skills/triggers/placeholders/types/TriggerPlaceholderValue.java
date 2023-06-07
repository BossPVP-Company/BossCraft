package com.bosspvp.api.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class TriggerPlaceholderValue extends TriggerPlaceholder {
    public TriggerPlaceholderValue(@NotNull BossPlugin plugin) {
        super(plugin,"trigger_value");
    }

    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        return List.of(
                new StaticPlaceholder("trigger_value", ()-> String.valueOf(data.value())),
                new StaticPlaceholder("triggervalue", ()-> String.valueOf(data.value())),
                new StaticPlaceholder("trigger", ()-> String.valueOf(data.value())),
                new StaticPlaceholder("value", ()-> String.valueOf(data.value())),
                new StaticPlaceholder("tv", ()-> String.valueOf(data.value())),
                new StaticPlaceholder("v", ()-> String.valueOf(data.value())),
                new StaticPlaceholder("t", ()-> String.valueOf(data.value()))
        );
    }
}
