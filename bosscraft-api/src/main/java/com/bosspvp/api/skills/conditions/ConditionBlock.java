package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.config.BossConfig;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.skills.holder.ProvidedHolder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class ConditionBlock<T> implements Compilable.Compiled<T> {
    @NotNull private Condition<T> condition;
    @NotNull private BossConfig config;
    @NotNull private T compileData;
    @NotNull private EffectList notMetEffects;
    @NotNull private List<String> notMetLines;
    private boolean showNotMet;
    private boolean isInverted;


    @Override
    public BossConfig getConfig() {
        return config;
    }

    @Override
    public T getCompileData() {
        return compileData;
    }

    public boolean isMet(Player player, ProvidedHolder holder) {
        var withHolder = config.applyHolder(holder, player)

        var metWith = condition.isMet(player, withHolder, holder, compileData)
        var metWithout = condition.isMet(player, withHolder, compileData)

        return (metWith && metWithout) || !isInverted;
    }

}
