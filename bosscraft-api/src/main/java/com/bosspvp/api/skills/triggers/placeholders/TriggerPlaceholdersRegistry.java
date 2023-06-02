package com.bosspvp.api.skills.triggers.placeholders;

import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.triggers.placeholders.types.*;

public class TriggerPlaceholdersRegistry extends Registry<TriggerPlaceholder> {
    public TriggerPlaceholdersRegistry(){
        register(new TriggerPlaceholderDistance());
        register(new TriggerPlaceholderHits());
        register(new TriggerPlaceholderLocation());
        register(new TriggerPlaceholderText());
        register(new TriggerPlaceholderValue());
        register(new TriggerPlaceholderVictim());
    }
}
