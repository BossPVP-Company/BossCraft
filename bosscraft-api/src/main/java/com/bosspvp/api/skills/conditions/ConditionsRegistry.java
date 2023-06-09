package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.bukkit.entity.Boss;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ConditionsRegistry extends Registry<Condition<?>> {
    private final BossPlugin plugin;

    public ConditionsRegistry(BossPlugin plugin){
        this.plugin = plugin;
    }

    

    /**
     * Compile a list of [configs] into a ConditionList in a given [context].
     */
    @NotNull
    public ConditionList compile(List<Config> configs, ViolationContext context){
        return new ConditionList(
                configs.stream().map(it->compile(it,context)).filter(Objects::nonNull).collect(Collectors.toList())
        );
    }
    /**
     * Compile a [cfg] into a ConditionBlock in a given [context].
     */
    public @Nullable ConditionBlock<?> compile(Config cfg, ViolationContext context){

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
}
