package com.bosspvp.api.skills.effects.executors;

import com.bosspvp.api.registry.Registrable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class ChainExecutorFactory implements Registrable {
    @Getter
    private final String id;
    public ChainExecutorFactory(@NotNull String id){
        this.id = id;
    }

    public abstract @NotNull ChainExecutor create();
}
