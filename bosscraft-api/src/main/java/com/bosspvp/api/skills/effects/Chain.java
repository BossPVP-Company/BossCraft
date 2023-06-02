package com.bosspvp.api.skills.effects;

import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class Chain {
    @Getter
    private final List<ChainElement<?>> list;
    @Getter
    private final ChainExecutor executor;
    @Getter
    private final int weight;
    public Chain(@NotNull List<ChainElement<?>> list, @NotNull ChainExecutor executor){
        this.list = list.stream().sorted(Comparator.comparingInt(it->it.getEffect().getRunOrder().getWeight())).toList();
        int weightSum = 0;
        for(ChainElement<?> entry : list){
            weightSum += entry.getEffect().getRunOrder().getWeight();
        }
        weight = weightSum;
        this.executor = executor;
    }
    public boolean trigger(DispatchedTrigger trigger,
                           ChainExecutor executor){
        return executor.execute(this, trigger);
    }
    public boolean trigger(DispatchedTrigger trigger){
        return this.trigger(trigger,executor);
    }
}
