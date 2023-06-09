package com.bosspvp.core.skills.effects.executors;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.effects.executors.ChainExecutorFactory;
import com.bosspvp.api.skills.effects.executors.ChainExecutorRegistry;
import com.bosspvp.core.skills.effects.executors.types.CycleExecutorFactory;
import com.bosspvp.core.skills.effects.executors.types.NormalExecutorFactory;
import com.bosspvp.core.skills.effects.executors.types.RandomExecutorFactory;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BossChainExecutorRegistry extends Registry<ChainExecutorFactory> implements ChainExecutorRegistry {
    @Getter
    private final BossPlugin plugin;

    public BossChainExecutorRegistry(@NotNull BossPlugin plugin){
        this.plugin = plugin;

        register(new CycleExecutorFactory());
        register(new NormalExecutorFactory());
        register(new RandomExecutorFactory());
    }

    @Override
    public @NotNull ChainExecutor getOrNormal(@Nullable String id) {
        ChainExecutorFactory factory = id != null ? get(id) : get("normal");
        if(factory==null) {
            factory = get("normal");
        }
        return factory.create();
    }

    @Override
    public Registry<ChainExecutorFactory> getRegistry() {
        return this;
    }
}
