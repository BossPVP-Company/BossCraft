package com.bosspvp.core.skills.counters;

import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.counters.Accumulator;
import com.bosspvp.api.skills.counters.Counter;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentResponse;
import com.bosspvp.api.skills.triggers.event.TriggerDispatchEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CounterHandler implements Listener {
    private final BossCountersRegistry registry;

    public CounterHandler(BossCountersRegistry registry){
        this.registry = registry;
    }

    @EventHandler
    public void handle(TriggerDispatchEvent event){
        HashMap<Counter, List<Accumulator>> counters = registry.getCounters();
        var trigger = event.getTrigger();
        var data = trigger.data();

        var player = trigger.player();
        var value = data.value();

        var applicableCounters = counters.entrySet().stream().
                filter((it) ->  it.getKey().getTrigger() == trigger.trigger()).collect(Collectors.toSet());

        for(var entry : applicableCounters){
            var counter = entry.getKey();
            var accumulators = entry.getValue();
            if(!counter.getConditions().areMet(player, data.holder())){
                continue;
            }
            if(!counter.getFilters().isMet(data)){
                continue;
            }
            var config = counter.getConfig();

            // Inject placeholders, totally not stolen from ElementLike
            config.addInjectablePlaceholder(trigger.placeholders());
            counter.getFilters().getList().stream().map(Compilable.Compiled::getConfig)
                    .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
            counter.getConditions().getList().stream().map(Compilable.Compiled::getConfig)
                    .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));


            EffectArgumentResponse response = counter.getArguments().checkMet(counter, trigger);
            if(!response.wasMet()){
                continue;
            }
            var multiplier = registry.getPlugin().getAPI().evaluate(
                    counter.getMultiplierExpression(),
                    new PlaceholderContext(
                            player,
                            null,
                            config,
                            new ArrayList<>()
                    )
            );
            response.met().forEach(it->it.ifMet(counter, trigger));
            response.notMet().forEach(it->it.ifNotMet(counter, trigger));

            for (Accumulator accumulator : accumulators) {
                accumulator.accept(player, value * multiplier);
            }
        }
    }
}
