package com.bosspvp.api.skills.filters;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.triggers.TriggerData;
import lombok.Getter;

public class FilterBlock<T,V> implements Compilable.Compiled<T>{
    @Getter
    private final Filter<T,V> filter;
    @Getter
    private final Config config;
    @Getter
    private final T compileData;
    public FilterBlock(Filter<T,V> filter, Config config, T compileData) {
        this.filter = filter;
        this.config = config;
        this.compileData = compileData;
    }
    public boolean isMet(TriggerData data) {
        return filter.isMet(data, this);
    }
}
