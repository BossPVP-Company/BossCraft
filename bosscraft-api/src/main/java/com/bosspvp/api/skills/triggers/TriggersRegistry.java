package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.registry.Registry;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class TriggersRegistry extends Registry<Trigger> {
    @Getter
    private static TriggersRegistry instance;
    private Registry<TriggerGroup> groupRegistry = new Registry<>();

    TriggersRegistry(){
        instance = this;
    }

    /**
     * Get a trigger by [id].
     * <p>
     * This will enable the trigger.
     */
    @Nullable
    public Trigger get(String id) {
        Trigger out = doGet(id);
        if (out == null) return null;
        out.setEnabled(true);
        return out;
    }

    @Nullable
    private Trigger doGet(String id) {
        for (TriggerGroup group : groupRegistry.values()) {
            if (id.startsWith(group.getPrefix() + "_")) {
                //clear prefix
                return group.create(id.substring(0, (group.getPrefix() + "_").length() - 1));
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
        return (it) -> new HashSet<>(it.getParameters().stream().map(
                param -> {
                    List<TriggerParameter> out = new ArrayList<>();
                    out.addAll(Arrays.asList(param.getInheritsFrom()));
                    out.add(param);
                    return out;
                }).toList()).containsAll(parameters);
    }
}
