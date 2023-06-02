package com.bosspvp.api.skills.effects.executors.types;

import com.bosspvp.api.skills.effects.Chain;
import com.bosspvp.api.skills.effects.ChainElement;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.effects.executors.ChainExecutorFactory;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.jetbrains.annotations.NotNull;

public class CycleExecutorFactory extends ChainExecutorFactory {
    public CycleExecutorFactory() {
        super("cycle");
    }

    @Override
    public ChainExecutor create() {
        return new CycleExecutor();
    }
    private static class CycleExecutor implements ChainExecutor{
        private int offset;
        @Override
        public boolean execute(@NotNull Chain chain, @NotNull DispatchedTrigger trigger) {
            offset %= chain.getList().size();
            boolean success = chain.getList().get(offset).trigger(trigger);
            offset++;
            return success;
        }
    }
}
