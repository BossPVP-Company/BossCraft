package com.bosspvp.api.skills.holder;

import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.EffectList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public interface Holder {

    String getId();
    ConditionList getConditionList();
    EffectList getEffectList();
}
