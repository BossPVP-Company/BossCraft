package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

public class TriggersRegistry extends Registry<Trigger> {
    private Registry<TriggerGroup> groupRegistry = new Registry<>();

    /**
     * Get a trigger by [id].
     *
     * This will enable the trigger.
     */
    @Nullable
    public Trigger get(String id) {
        return doGet(id)?.apply {
            isEnabled = true
        }
    }

    @Nullable
    private Trigger doGet(String id) {
        for (TriggerGroup group : groupRegistry.values()) {
            if (id.startsWith("${group.prefix}_")) {
                return group.create(id.removePrefix("${group.prefix}_"))
            }
        }

        return super.get(id);
    }

    /**
     * Register a new [triggerGroup].
     */
    public void register(TriggerGroup triggerGroup) {
        groupRegistry.register(triggerGroup);
    }

    /**
     * Get a predicate requiring certain trigger parameters.
     */
    public Predicate<Trigger> withParameters(Set<TriggerParameter> parameters) {
        return (it)->{
                it.parameters.flatMap { param -> param.inheritsFrom.toList().plusElement(param) }.containsAll(parameters)
        };
    }
}
