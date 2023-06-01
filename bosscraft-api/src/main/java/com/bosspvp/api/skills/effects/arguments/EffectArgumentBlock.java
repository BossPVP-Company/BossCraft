package com.bosspvp.api.skills.effects.arguments;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EffectArgumentBlock<T> implements Compilable.Compiled<T> {
    @Getter
    private EffectArgument<T> argument;
    @Getter
    private Config config;
    @Getter
    private T compileData;


    public boolean isMet(ConfigurableElement element,
                         DispatchedTrigger trigger){
        return argument.isMet(element,trigger,compileData);
    }
    public void ifMet(ConfigurableElement element,
                      DispatchedTrigger trigger){
        argument.ifMet(element,trigger,compileData);
    }
    public void ifNotMet(ConfigurableElement element,
                         DispatchedTrigger trigger){
        argument.ifNotMet(element,trigger,compileData);
    }

}
