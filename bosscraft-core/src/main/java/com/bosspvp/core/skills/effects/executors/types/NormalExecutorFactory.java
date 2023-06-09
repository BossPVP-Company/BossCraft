package com.bosspvp.core.skills.effects.executors.types;

import com.bosspvp.api.skills.effects.Chain;
import com.bosspvp.api.skills.effects.ChainElement;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.effects.executors.ChainExecutorFactory;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.jetbrains.annotations.NotNull;

public class NormalExecutorFactory extends ChainExecutorFactory {
    public NormalExecutorFactory() {
        super("normal");
    }

    @Override
    public @NotNull ChainExecutor create() {
        return new NormalExecutor();
    }
    private static class NormalExecutor implements ChainExecutor{

        @Override
        public boolean execute(@NotNull Chain chain, @NotNull DispatchedTrigger trigger) {
            boolean bool = true;
            for(ChainElement<?> entry : chain.getList()){
                if(!entry.trigger(trigger)){
                    bool = false;
                }
            }
            return bool;
        }
    }
}
