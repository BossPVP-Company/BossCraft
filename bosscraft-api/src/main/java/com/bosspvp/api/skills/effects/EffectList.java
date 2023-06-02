package com.bosspvp.api.skills.effects;

import com.bosspvp.api.skills.conditions.ConditionBlock;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
public class EffectList {
    @Getter
    private final List<EffectBlock> list;

    public EffectList(@NotNull List<EffectBlock> list){
        this.list = list.stream().sorted(Comparator.comparingInt(EffectBlock::getWeight)).toList();
    }

    public void trigger(DispatchedTrigger trigger){
        list.forEach(it->it.trigger(trigger));
    }

}
