package com.bosspvp.core.skills.effects.arguments;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.effects.arguments.EffectArgument;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentBlock;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentsRegistry;
import com.bosspvp.core.skills.effects.arguments.types.ArgumentChance;
import com.bosspvp.core.skills.effects.arguments.types.ArgumentPrice;
import com.bosspvp.core.skills.effects.arguments.types.ArgumentRequire;
import com.bosspvp.api.skills.violation.ViolationContext;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BossEffectArgumentsRegistry extends Registry<EffectArgument<?>> implements EffectArgumentsRegistry {
    @Getter
    private final BossPlugin plugin;
    public BossEffectArgumentsRegistry(@NotNull BossPlugin plugin){
        this.plugin = plugin;
        register(new ArgumentRequire(plugin));
        register(new ArgumentChance(plugin));
        register(new ArgumentPrice(plugin));
    }

    @Override
    public EffectArgumentList compile(@NotNull Config config, @NotNull ViolationContext context){
        var blocks = new ArrayList<EffectArgumentBlock<?>>();

        for (String key : config.getKeys(false)) {
            var argument = get(key);
            if(argument==null) continue;
            var block = makeBlock(argument, config, context);
            if(block == null) continue;
            blocks.add(block);
        }

        return new EffectArgumentList(blocks);
    }
    private<T> EffectArgumentBlock<T> makeBlock(EffectArgument<T> argument,
                                                Config config,
                                                ViolationContext context){
        if (!argument.checkConfig(config, context)) {
            return null;
        }
        try {
            var compileData = argument.makeCompileData(config, context);
            return new EffectArgumentBlock<T>(argument, config, compileData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Registry<EffectArgument<?>> getRegistry() {
        return this;
    }
}
