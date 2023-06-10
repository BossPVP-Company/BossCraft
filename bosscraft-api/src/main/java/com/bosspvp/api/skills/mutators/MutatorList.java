package com.bosspvp.api.skills.mutators;

import com.bosspvp.api.skills.triggers.TriggerData;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

public class MutatorList {
    @Getter
    private final List<MutatorBlock<?>> list;
    public MutatorList(List<MutatorBlock<?>> mutators) {
        this.list = mutators.stream().sorted(Comparator.comparingInt(a -> a.getMutator().getRunOrder().getWeight())).toList();
    }
    public TriggerData mutate(TriggerData data) {
        TriggerData current = data;
        for (MutatorBlock<?> block : list) {
            current = block.mutate(current);
        }
        return current;
    }
}
