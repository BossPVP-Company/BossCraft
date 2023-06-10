package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentResponse;
import com.bosspvp.api.skills.filters.FilterList;
import com.bosspvp.api.skills.mutators.MutatorList;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ElementLike implements ConfigurableElement {
    @Getter
    private final BossPlugin plugin;
    public ElementLike(BossPlugin plugin){
        this.plugin = plugin;
    }

    public abstract EffectArgumentList getArguments();
    public abstract ConditionList getConditions();
    public abstract MutatorList getMutators();
    public abstract FilterList getFilters();



    public abstract boolean isSupportDelay();

    /**
     * If the element is its own chain, (e.g. has an ID specified directly at the top level).
     */
    public boolean isElementOwnChain(){
        return false;
    }
    /**
     * Mutate, filter, and then trigger.
     */
    public boolean trigger(DispatchedTrigger trigger) {
        // If own chain, defer all to elements.
        if (isElementOwnChain()) {
            return doTrigger(trigger);
        }

        Config config = getConfig();
        // Extra initial injection, otherwise it's not possible to use injections
        // in the repeat configs.
        config.addInjectablePlaceholder(trigger.placeholders());
        PlaceholderContext context = trigger.data().toPlaceholderContext(config);
        int repeatTimes = (int) Math.max(config.getEvaluated("repeat.times", context), 1);
        double repeatStart = config.getEvaluated("repeat.start", context);
        double repeatIncrement = config.getEvaluated("repeat.increment", context);
        final double[] repeatCount = {repeatStart};

        trigger.addPlaceholder("repeat_times", String.valueOf(repeatTimes));
        trigger.addPlaceholder("repeat_start", String.valueOf(repeatStart));
        trigger.addPlaceholder("repeat_increment", String.valueOf(repeatIncrement));
        trigger.addPlaceholderDynamic("repeat_count",()-> String.valueOf(repeatCount[0]));
        //update context
        context = trigger.data().toPlaceholderContext(config);
        long delay = (long) Math.max(
                isSupportDelay()?config.getEvaluated("delay", context) : 0,
                0
        );

        getMutators().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        var data = getMutators().mutate(trigger.data());

        // Inject placeholders everywhere after mutation
        trigger.generateTriggerPlaceholders();
        //inject placeholders
        getArguments().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        getConditions().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        getMutators().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        getFilters().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        //-----------------

        // Filter
        var filterResult = config.getBool("filters_before_mutation") ?
                getFilters().isMet(trigger.data()) : getFilters().isMet(data);

        if (!filterResult) {
            return false;
        }
        if(!shouldTrigger(trigger.copyToBuilder().data(data).build())){
            return false;
        }
        EffectArgumentResponse response = getArguments().checkMet(this, trigger);
        // Only execute not met effects if the arguments were met.
        if (response.wasMet()) {
            // Check conditions
            if (!getConditions().areMetAndTrigger(trigger)) {
                return false;
            }
        } else {
            response.notMet().forEach(it->it.ifNotMet(this, trigger) );
            return false;
        }

        final boolean[] didTrigger = {false};

        // Can't delay initial execution for things that modify events.
        if (delay == 0L) {
            for(int i = 0; i<repeatTimes; i++){
                if(doTrigger(trigger.copyToBuilder().data(getMutators().mutate(data)).build())){
                    didTrigger[0] = true;
                }
                repeatCount[0] += repeatIncrement;
            }
        } else {
            // Delay between each repeat.
            new BukkitRunnable() {
                int repeats = 0;
                @Override
                public void run() {
                    repeats++;
                    if(doTrigger(trigger.copyToBuilder().data(getMutators().mutate(data)).build())){
                        didTrigger[0] = true;
                    }
                    repeatCount[0] += repeatIncrement;
                    if (repeats >= repeatTimes) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, delay, delay);
        }


        if (didTrigger[0] || delay > 0) {
            response.met().forEach (it->it.ifMet(this, trigger));
        }

        return didTrigger[0] || delay > 0;
    }
    protected abstract boolean doTrigger(DispatchedTrigger trigger);

    protected boolean shouldTrigger(DispatchedTrigger trigger) {
        return true;
    }
}
