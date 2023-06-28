package com.bosspvp.core.skills.effects.arguments.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.effects.arguments.EffectArgument;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.utils.MathUtils;
import com.bosspvp.api.utils.NumberUtils;
import org.jetbrains.annotations.NotNull;

public class ArgumentChance extends EffectArgument<Compilable.NoCompileData> {
    public ArgumentChance(@NotNull BossPlugin plugin) {
        super(plugin, "chance");
    }

    @Override
    public boolean isMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        return MathUtils.randDouble(0.0, 100.0) <= element.getConfig().getSubsection("args").getEvaluated("chance",
                trigger.data().toPlaceholderContext(element.getConfig())
        );
    }
}
