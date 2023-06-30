package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EffectMultiplyDrops extends Effect<Compilable.NoCompileData> {
    public EffectMultiplyDrops(@NotNull BossPlugin plugin) {
        super(plugin, "multiply_drops");
        setArguments(arguments ->
                arguments.require(List.of("multiplier", "fortune"),
                        "You must specify a multiplier or level of fortune to mimic!"
                )
        );
    }
    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        if(!(data.event() instanceof BlockDropItemEvent) &&
                !(data.event() instanceof EntityDropItemEvent)) return false;

        double multiplier = 1;
        if(config.hasPath("fortune")) {
            var fortune = (int) config.getEvaluated("fortune", data.toPlaceholderContext(config));
            multiplier = (int)(Math.random() * (fortune - 1) + 1.1);
        }else if(config.hasPath("multiplier")) {
            multiplier = (int) config.getEvaluated("multiplier", data.toPlaceholderContext(config));
        }

        /*
        event.addModifier {
            var matches = true
            if (config.has("on_items")) {
                val items = config.getStrings("on_items").map { string -> Items.lookup(string) }
                matches = items.any { test -> test.matches(it) }
            }

            if (it.maxStackSize > 1 && matches) {
                it.amount *= multiplier
            }

            DropResult(it, 0)
        }

        return true*/
        return true;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.EVENT);
    }
}
