package com.bosspvp.api.skills.effects.executors;

import com.bosspvp.api.skills.effects.executors.types.CycleExecutorFactory;
import com.bosspvp.api.skills.effects.executors.types.NormalExecutorFactory;
import com.bosspvp.api.skills.effects.executors.types.RandomExecutorFactory;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class ChainExecutorRegistry {
    private HashMap<String,ChainExecutorFactory> registry = new HashMap<>();

    public ChainExecutorRegistry(){
        register(new CycleExecutorFactory());
        register(new NormalExecutorFactory());
        register(new RandomExecutorFactory());
    }
    public void register(ChainExecutorFactory factory) {
        registry.put(factory.getId(),factory);
    }

    /**
     * Get executor by ID, or null if invalid ID is provided.
     *
     * Returns normal if the ID is null.
     */
    public ChainExecutor getByID(@Nullable String id) {
        ChainExecutorFactory factory = id != null ? registry.get(id) : registry.get("normal");
        if(factory==null) {
            factory = registry.get("normal");
        }
        return factory.create();
    }
}
