package com.bosspvp.api.skills.mutators;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.triggers.TriggerData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class MutatorBlock<T> implements Compilable.Compiled<T> {

    @Getter
    private final Mutator<T> mutator;
    @Getter
    private final Config config;
    @Getter
    private final T compileData;
    public MutatorBlock(@NotNull Mutator<T> mutator,
                        @NotNull Config config,
                        @NotNull T compileData) {
        this.mutator = mutator;
        this.config = config;
        this.compileData = compileData;
    }

    public TriggerData mutate(TriggerData data) {
        return mutator.mutate(data, this);
    }
}
