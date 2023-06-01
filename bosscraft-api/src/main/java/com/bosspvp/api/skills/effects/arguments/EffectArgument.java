package com.bosspvp.api.skills.effects.arguments;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.effects.RunOrder;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.jetbrains.annotations.NotNull;

public abstract class EffectArgument<T> extends Compilable<T> {

    public EffectArgument(String id){
        super(id);
    }

    RunOrder getRunOrder(){
        return RunOrder.NORMAL;
    }

    public boolean isMet(@NotNull ConfigurableElement element,
                         @NotNull DispatchedTrigger trigger,
                         @NotNull T compileData){
        return true;
    }
    public void ifMet(@NotNull ConfigurableElement element,
                      @NotNull DispatchedTrigger trigger,
                      @NotNull T compileData){

    }
    public void ifNotMet(@NotNull ConfigurableElement element,
                         @NotNull DispatchedTrigger trigger,
                         @NotNull T compileData){

    }
}
