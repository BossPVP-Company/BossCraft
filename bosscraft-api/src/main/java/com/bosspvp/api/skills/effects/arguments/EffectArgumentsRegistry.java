package com.bosspvp.api.skills.effects.arguments;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.effects.arguments.types.ArgumentRequire;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EffectArgumentsRegistry extends Registry<EffectArgument<?>> {
    public EffectArgumentsRegistry(@NotNull BossPlugin plugin){
        register(new ArgumentRequire(plugin));
    }

    public EffectArgumentList compile(Config config, ViolationContext context){
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

}
