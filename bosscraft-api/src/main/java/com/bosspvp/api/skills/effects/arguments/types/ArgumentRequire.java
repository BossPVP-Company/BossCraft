package com.bosspvp.api.skills.effects.arguments.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.effects.arguments.EffectArgument;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.jetbrains.annotations.NotNull;

public class ArgumentRequire extends EffectArgument<Compilable.NoCompileData> {
    public ArgumentRequire(@NotNull BossPlugin plugin) {
        super(plugin,"require");
    }

    @Override
    public boolean isMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, Compilable.@NotNull NoCompileData compileData) {
        return element.getConfig().
                getEvaluated("require",
                        trigger.data().toPlaceholderContext(element.getConfig())
                ) == 1.0;
    }
}
