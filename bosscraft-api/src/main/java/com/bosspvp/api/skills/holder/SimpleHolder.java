package com.bosspvp.api.skills.holder;

import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.EffectList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SimpleHolder implements Holder{
    @Getter
    @NotNull
    private String id;
    @Getter @NotNull
    private ConditionList conditionList;
    @Getter @NotNull
    private EffectList effectList;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof SimpleHolder holder)){
            return false;
        }
        return holder.id.equals(id);
    }
}
