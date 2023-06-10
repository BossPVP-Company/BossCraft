package com.bosspvp.api.skills.filters;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.RunOrder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;

public abstract class Filter<T,V>  extends Compilable<T> {
    public Filter(@NotNull BossPlugin plugin, @NotNull String id){
        super(plugin, id);
    }


    public boolean isMet(TriggerData data, FilterBlock<T,V> block){
        Config cfg = block.getConfig();
        boolean regularPresent = cfg.hasPath(getId());
        boolean inversePresent = cfg.hasPath("not_" + getId());
        if (!regularPresent && !inversePresent) {
            return true;
        }
        if (inversePresent) {
            return !isMet(data, getValue(cfg, data, "not_" + getId()),block.getCompileData());
        } else {
            return isMet(data, getValue(cfg, data, getId()), block.getCompileData());
        }
    }

    @Override
    public final T makeCompileData(Config config, ViolationContext context) throws Exception {
        throw new UnsupportedOperationException("Use makeCompileData(Config, ViolationContext, V) instead!");
    }

    public T makeCompileData(Config config, ViolationContext context, V value) throws Exception {
        return (T) new NoCompileData();
    }
    protected abstract boolean isMet(TriggerData data, V value, T compileData);
    public abstract V getValue(Config config, TriggerData data, String key);
    public RunOrder getRunOrder(){
        return RunOrder.NORMAL;
    }
}
