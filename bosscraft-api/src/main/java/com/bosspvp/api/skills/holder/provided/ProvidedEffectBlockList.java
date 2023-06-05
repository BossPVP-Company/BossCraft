package com.bosspvp.api.skills.holder.provided;

import com.bosspvp.api.skills.effects.EffectBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record ProvidedEffectBlockList(ProvidedHolder holder, Set<EffectBlock> effects) {

    public List<ProvidedEffectBlock> flatten(){
        List<ProvidedEffectBlock> list = new ArrayList<>();
        for (EffectBlock effect : effects) {
            list.add(new ProvidedEffectBlock(holder, effect));
        }
        return list;
    }
}
