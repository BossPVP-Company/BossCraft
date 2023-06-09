package com.bosspvp.api.skills.effects.executors;

import com.bosspvp.api.skills.effects.Chain;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.jetbrains.annotations.NotNull;

public interface ChainExecutor {
    /**
     * Execute the chain.
     *
     * @param chain   The chain to execute.
     * @param trigger The trigger that dispatched the chain.
     * @return Whether the chain was executed successfully.
     */
    boolean execute(@NotNull Chain chain,
                    @NotNull DispatchedTrigger trigger);
}
