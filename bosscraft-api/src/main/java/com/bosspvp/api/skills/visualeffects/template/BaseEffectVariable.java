package com.bosspvp.api.skills.visualeffects.template;

import com.bosspvp.api.skills.visualeffects.VisualEffectVariable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.function.Function;

public class BaseEffectVariable<T> implements VisualEffectVariable<T> {


    @Getter private T value;
    private boolean forceCloneValue = false;

    private Function<String, T> valueFromString;

    public BaseEffectVariable(@NotNull T value, @NotNull Function<String, T> valueFromString){
        this.value=value;
        this.valueFromString = valueFromString;
    }

    /**
     * Whether to force clone the value using java reflections.
     *
     * Use it with cautious. May produce lags
     *
     */
    public BaseEffectVariable<T> setForceCloneValue(boolean value){
        forceCloneValue=value;
        return this;
    }
    @Override
    public @NotNull BaseEffectVariable<T> setValue(@NotNull T value) {
        this.value=value;
        return this;
    }

    @Override
    public @NotNull VisualEffectVariable<T> setValueFromString(@NotNull String value) {
        return setValue(valueFromString.apply(value));
    }

    @Override
    public BaseEffectVariable<T> clone() throws CloneNotSupportedException {
        BaseEffectVariable<T> variable = (BaseEffectVariable<T>) (super.clone());
        if(forceCloneValue && value != null && value instanceof Cloneable){
            try {
                Method method =value.getClass().getMethod("clone");
                method.setAccessible(true);
                setValue((T)method.invoke(value));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return variable;

    }
}
