package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.skills.holder.ProvidedHolder;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ConditionList {
    @NotNull @Getter
    private List<ConditionBlock<?>> list;

    /**
     * Get if all conditions are met.
     */
    public boolean areMet(Player player, ProvidedHolder holder){
        for(ConditionBlock<?> entry : list){
            if(!entry.isMet(player,holder)) return false;
        }
        return true;
    }

    /**
     * Get if all conditions are met, triggering effects if not.
     */
    //@TODO
    public boolean areMetAndTrigger(DispatchedTrigger trigger){
        List<ConditionBlock<?>> notMet = list.stream().filter(it->!it.isMet(trigger.player(),trigger.data().holder()))
                .toList();
        if(notMet.isEmpty()){
            return true;
        }
        notMet.forEach(it->it.getNotMetEffects().trigger(trigger));
        return false;
    }
    /**
     * Get if any conditions are not met and should be shown.
     */
    public boolean isShowingAnyNotMet(Player player, ProvidedHolder holder){
        return !getNotMetLines(player,holder).isEmpty();
    }
    /**
     * Get all not met lines.
     */
    public List<String> getNotMetLines(Player player, ProvidedHolder holder){
        return list.stream().filter(it->it.isMet(player,holder))
                .flatMap(it-> it.getNotMetLines().stream())
                .map(StringUtils::format) //@TODO format placeholders
                .collect(Collectors.toList());
    }


}
