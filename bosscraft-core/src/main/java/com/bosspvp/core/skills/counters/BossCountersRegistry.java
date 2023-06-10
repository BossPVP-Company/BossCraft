package com.bosspvp.core.skills.counters;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.counters.Accumulator;
import com.bosspvp.api.skills.counters.Counter;
import com.bosspvp.api.skills.counters.CountersRegistry;
import com.bosspvp.api.skills.filters.FilterList;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossCountersRegistry implements CountersRegistry {
    @Getter
    private final BossPlugin plugin;
    private final SkillsManager skillsManager;
    @Getter
    private final HashMap<Counter, List<Accumulator>> counters = new HashMap<>();

    public BossCountersRegistry(@NotNull BossPlugin plugin) {
        this.plugin = plugin;
        this.skillsManager = plugin.getSkillsManager();
    }
    @Override
    public @Nullable Counter compile(@NotNull Config config, @NotNull ViolationContext context) {
        //@TODO  val config = cfg.separatorAmbivalent()
        var id = config.getStringOrNull("trigger");
        if (id == null) {
            id = config.getString("id");
        }
        var trigger =skillsManager.getTriggersRegistry().get(id);
        if (trigger == null) {
            context.log(
                new ConfigViolation(
                    "trigger",
                    "Invalid trigger ID specified!"
                )
            );
            return null;
        }
        var conditions = skillsManager.getConditionsRegistry().compile(
            config.getSubsectionList("conditions"),
            context.with("conditions")
        );
        var filterSection = config.getSubsection("filters");
        FilterList filters;
        if(filterSection == null) {
            filters = new FilterList(new ArrayList<>());
        }else {
            filters = skillsManager.getFilterRegistry().compile(
                    filterSection,
                    context.with("filters")
            );
        }
        var args = config.getSubsection("args");
        var arguments = skillsManager.getEffectArgumentsRegistry().compile(
            args,
            context.with("args")
        );
        var multiplierExpression = config.getStringOrNull("multiplier");
        if(multiplierExpression == null) {
            multiplierExpression = "1";
        }
        return new Counter(
            trigger,
            conditions,
            filters,
            config,
            arguments,
            multiplierExpression
        );
    }

    @Override
    public void bindCounter(@NotNull Counter counter, @NotNull Accumulator accumulator) {
        List<Accumulator> accumulators = counters.get(counter);
        if (accumulators == null) {
            accumulators = new ArrayList<>();
        }
        accumulators.add(accumulator);
        counters.put(counter, accumulators);
    }

    @Override
    public void unbindCounter(@NotNull Counter counter) {
        counters.remove(counter);
    }


     /*fun compile(
        cfg: Config,
        context: ViolationContext
    ): Counter? {
        val config = cfg.separatorAmbivalent()

        // Legacy support for 'id' instead of 'trigger'
        val id = if (config.has("trigger")) config.getString("trigger") else config.getString("id")

        val trigger = Triggers[id]

        if (trigger == null) {
            context.log(
                ConfigViolation(
                    "trigger",
                    "Invalid trigger ID specified!"
                )
            )

            return null
        }

        val conditions = Conditions.compile(
            config.getSubsections("conditions"),
            context.with("conditions")
        )

        val filters = Filters.compile(
            config.getSubsection("filters"),
            context.with("filters")
        )

        val args = config.getSubsection("args")
        val arguments = EffectArguments.compile(
            args,
            context.with("args")
        )

        val multiplierExpression = if (config.has("multiplier")) {
            config.getString("multiplier")
        } else "1" // lmao

        return Counter(
            trigger,
            conditions,
            filters,
            args,
            arguments,
            multiplierExpression
        )
    }*/
}
