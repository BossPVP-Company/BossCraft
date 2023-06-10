package com.bosspvp.api.skills.mutators;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.RunOrder;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.jetbrains.annotations.NotNull;

public abstract class Mutator<T> extends Compilable<T> {
    public Mutator(@NotNull BossPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }


    /**
     * Mutate the trigger data.
     *
     * @param data The data.
     * @param block The block.
     * @return The modified data.
     */
    TriggerData mutate(TriggerData data,
                       MutatorBlock<T> block){

        return mutate(data, block.getConfig(), block.getCompileData());
    }

    /**
     * Mutate the trigger data.
     *
     * @param data The data.
     * @param config The config.
     * @param compileData The compile data.
     * @return The modified data.
     */
    protected abstract TriggerData mutate(
            TriggerData data,
            Config config,
            T compileData
    );
    public RunOrder getRunOrder(){
        return RunOrder.NORMAL;
    }

}
