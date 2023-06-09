package com.bosspvp.core.skills.conditions;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.conditions.Condition;
import com.bosspvp.api.skills.conditions.ConditionBlock;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.conditions.ConditionsRegistry;
import com.bosspvp.core.skills.conditions.types.ConditionHasPermission;
import com.bosspvp.core.skills.conditions.types.ConditionInAir;
import com.bosspvp.core.skills.conditions.types.ConditionInBiome;
import com.bosspvp.core.skills.conditions.types.ConditionInBlock;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BossConditionsRegistry extends Registry<Condition<?>> implements ConditionsRegistry {
    @Getter
    private final BossPlugin plugin;


    @Override
    public @NotNull ConditionList compile(@NotNull List<Config> configs, @NotNull ViolationContext context){
        return new ConditionList(
                configs.stream().map(it->compile(it,context)).filter(Objects::nonNull).collect(Collectors.toList())
        );
    }

    @Override
    public @Nullable ConditionBlock<?> compile(@NotNull Config cfg, @NotNull ViolationContext context){

        //@TODO cfg.separatorAmbivalent();
        Config config = cfg;

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
        try {
            var compileData = condition.makeCompileData(config, context);
            //@TODO
            var notMetEffects = plugin.getSkillsManager()
                    .getEffectsRegistry().compile(
                            config.getSubsectionList("not-met-effects"),
                            context.with("not-met-effects")
                    );
            return new ConditionBlock<T>(
                    condition,
                    config,
                    compileData,
                    notMetEffects,
                    config.getStringList("not-met-lines"),
                    config.getBool("show-not-met"),
                    config.getBool("inverse")
            );
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public Registry<Condition<?>> getRegistry() {
        return this;
    }

    public BossConditionsRegistry(BossPlugin plugin){
        this.plugin = plugin;
        register(new ConditionInAir(plugin));
        register(new ConditionInBiome(plugin));
        register(new ConditionHasPermission(plugin));
        register(new ConditionInBlock(plugin));
    }
}
