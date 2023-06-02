package com.bosspvp.api.skills.effects.executors;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class ChainExecutorFactory {
    @Getter
    private final String id;
    public ChainExecutorFactory(@NotNull String id){
        this.id = id;
    }
    public abstract ChainExecutor create();
}
