package com.bosspvp.api.skills.effects.executors.types;

import com.bosspvp.api.skills.effects.Chain;
import com.bosspvp.api.skills.effects.ChainElement;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.effects.executors.ChainExecutorFactory;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.utils.MathUtils;
import org.jetbrains.annotations.NotNull;

public class RandomExecutorFactory extends ChainExecutorFactory {
    public RandomExecutorFactory() {
        super("random");
    }

    @Override
    public ChainExecutor create() {
        return new RandomExecutor();
    }
    private static class RandomExecutor implements ChainExecutor{

        @Override
        public boolean execute(@NotNull Chain chain, @NotNull DispatchedTrigger trigger) {
            int index = MathUtils.randInt(0,chain.getList().size()-1);
            return chain.getList().get(index).trigger(trigger);
        }
    }
}
