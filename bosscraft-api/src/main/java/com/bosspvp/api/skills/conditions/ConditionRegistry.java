package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.config.impl.BossConfigOkaeri;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ConditionRegistry extends Registry<Condition<?>> {


    /**
     * Compile a list of [configs] into a ConditionList in a given [context].
     */
    @NotNull
    public ConditionList compile(List<BossConfigOkaeri> configs, ViolationContext context){
        return new ConditionList(
                configs.stream().map(it->compile(it,context)).collect(Collectors.toList())
        );
    }

    /**
     * Compile a [cfg] into a ConditionBlock in a given [context].
     */
    public @Nullable ConditionBlock<?> compile(Config cfg, ViolationContext context){
        //@TODO
        var config = cfg.separatorAmbivalent();

        var condition = get(config.getString("id"));

        if (condition == null) {
            context.log(new ConfigViolation("id", "Invalid condition ID specified!"));
            return null;
        }

        return makeBlock(condition, config.getSubsection("args"), context.with("args"));
    }

    private<T> ConditionBlock<T> makeBlock(@NotNull Condition<T> condition,
                                           @NotNull Config config,
                                           @NotNull ViolationContext context){
        if(!condition.checkConfig(config,context)){
            return null;
        }
        var compileData = condition.makeCompileData(config,context);
        //@TODO
        var notMetEffects = Effects.compile(
                config.getSubsection("not-met-effects"),
                context.with("not-met-effects")
        );
        return new ConditionBlock(
                condition,
                config,
                compileData,
                notMetEffects,
                config.getStringList("not-met-lines"),
                config.getBool("show-not-met"),
                config.getBool("inverse")
        );

    }
}
