package com.bosspvp.api.skills.effects.arguments;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EffectArgumentList {
    @Getter
    private List<EffectArgumentBlock<?>> list;

    public EffectArgumentList(List<EffectArgumentBlock<?>> list){
        list = list.stream().sorted(Comparator.comparingInt(it -> it.getArgument().getRunOrder().getWeight()))
                .collect(Collectors.toList());

        this.list = list;

    }

    public EffectArgumentResponse checkMet(ConfigurableElement element,
                                           DispatchedTrigger trigger){
        var met = new ArrayList<EffectArgumentBlock<?>>();
        var notMet = new ArrayList<EffectArgumentBlock<?>>();

        for (EffectArgumentBlock<?> argument : list) {
            var isMet = argument.isMet(element, trigger);

            if (isMet) {
                met.add(argument);
            } else {
                notMet.add(argument);
            }
        }

        return new EffectArgumentResponse(
                notMet.isEmpty(),
                met,
                notMet
        );
    }




}
