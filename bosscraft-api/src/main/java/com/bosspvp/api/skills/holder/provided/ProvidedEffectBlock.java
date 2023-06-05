package com.bosspvp.api.skills.holder.provided;

import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.effects.EffectBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record ProvidedEffectBlock(ProvidedHolder holder, EffectBlock effect) implements Comparable<ProvidedEffectBlock> {
    @Override
    public int compareTo(@NotNull ProvidedEffectBlock other) {
        return this.effect.getWeight() - other.effect.getWeight();
    }
}
