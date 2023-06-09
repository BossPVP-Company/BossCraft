package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.EffectList;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class ConditionBlock<T> implements Compilable.Compiled<T> {

    @NotNull @Getter
    private Condition<T> condition;
    @NotNull @Getter
    private Config config;
    @NotNull @Getter
    private T compileData;
    @NotNull @Getter
    private EffectList notMetEffects;
    @NotNull @Getter
    private List<String> notMetLines;
    @Getter
    private boolean showNotMet;
    @Getter
    private boolean isInverted;



    public boolean isMet(Player player, ProvidedHolder holder) {
        var withHolder = config; //@TODO placeholders inject

        var metWith = condition.isMet(player, withHolder, holder, compileData);
        var metWithout = condition.isMet(player, withHolder, compileData);

        return (metWith && metWithout) != isInverted;
    }

}
