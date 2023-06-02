package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentResponse;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ElementLike implements ConfigurableElement {
    public abstract EffectArgumentList getArguments();
    public abstract ConditionList getConditions();
    //@TODO
    /*abstract val mutators: MutatorList
    abstract val filters: FilterList*/

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

        var data = trigger.data();
        //@TODO
        // Initial injection into mutators
        //mutators.map { it.config }.forEach { it.addInjectablePlaceholder(trigger.placeholders) }

        //val data = mutators.mutate(trigger.data)

        // Inject placeholders everywhere after mutation
        trigger.generateTriggerPlaceholders(); //(data) - add this argument when mutators added;
        //effects
        getArguments().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        //conditions
        getConditions().getList().stream().map(Compilable.Compiled::getConfig)
                .forEach(it->it.addInjectablePlaceholder(trigger.placeholders()));
        //@TODO mutators
        //@TODO filters
        //-----------------

        //@TODO
        // Filter
       /* val filterResult = if (config.getBool("filters_before_mutation")) {
            filters.isMet(trigger.data());
        } else {
            filters.isMet(data);
        }

        if (!filterResult) {
            return false;
        }

       */
        //@TODO copy
        /*if (!shouldTrigger(trigger.copy(data = data))) {
            return false;
        }*/

        //var (argumentsMet, met, notMet)
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

       /* fun trigger() {
            // Set to true if triggered.
            didTrigger = if (doTrigger(
                    trigger.copy(
                            // Mutate again here for each repeat.
                            data = mutators.mutate(trigger.data)
                    )
            )
            ) true else didTrigger

            repeatCount += repeatIncrement;
        }*/

        // Can't delay initial execution for things that modify events.
        if (delay == 0L) {
            for(int i = 0; i<repeatTimes; i++){
                if(doTrigger(trigger)){
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
                    if(doTrigger(trigger)){
                        didTrigger[0] = true;
                    }
                    repeatCount[0] += repeatIncrement;
                    if (repeats >= repeatTimes) {
                        cancel();
                    }
                }
            }.runTaskTimer(BossAPI.getInstance().getCorePlugin(), delay, delay);
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
