package com.bosspvp.api.skills.counters;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.filters.FilterList;
import com.bosspvp.api.skills.triggers.Trigger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Counter implements ConfigurableElement {
    @Getter
    private final CountersRegistry countersRegistry;
    @Getter
    private final Trigger trigger;
    @Getter
    private final ConditionList conditions;
    @Getter
    private final FilterList filters;
    @Getter
    private final Config config;
    @Getter
    private final EffectArgumentList arguments;
    @Getter
    private final String multiplierExpression;

    private final UUID uuid = UUID.randomUUID();

    public Counter(@NotNull Trigger trigger,
                   @NotNull ConditionList conditions,
                   @NotNull FilterList filters,
                   @NotNull Config config,
                   @NotNull EffectArgumentList arguments,
                   @NotNull String multiplierExpression) {
        this.trigger = trigger;
        this.conditions = conditions;
        this.filters = filters;
        this.config = config;
        this.arguments = arguments;
        this.multiplierExpression = multiplierExpression;
        this.countersRegistry = trigger.getPlugin().getSkillsManager().getCountersRegistry();
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    public void bind(Accumulator accumulator) {
        countersRegistry.bindCounter(this, accumulator);
    }

    public void unbind() {
        countersRegistry.unbindCounter(this);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Counter counter)) return false;

        return getUUID().equals(counter.getUUID());
    }
    @Override
    public int hashCode() {
        return getUUID().hashCode();
    }
}
