package com.bosspvp.api.skills.effects.executors;

import com.bosspvp.api.skills.effects.Chain;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.jetbrains.annotations.NotNull;

public interface ChainExecutor {
    boolean execute(@NotNull Chain chain,
                    @NotNull DispatchedTrigger trigger);
}
