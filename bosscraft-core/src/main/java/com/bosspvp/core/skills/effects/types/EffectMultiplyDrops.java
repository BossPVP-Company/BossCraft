package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.events.drop.DropResult;
import com.bosspvp.api.events.drop.EditableDropEvent;
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
        if(!(data.event() instanceof EditableDropEvent event)) {
            return false;
        }

        double multiplier;
        if(config.hasPath("fortune")) {
            var fortune = (int) config.getEvaluated("fortune", data.toPlaceholderContext(config));
            multiplier = (int)(Math.random() * (fortune - 1) + 1.1);
        }else if(config.hasPath("multiplier")) {
            multiplier = (int) config.getEvaluated("multiplier", data.toPlaceholderContext(config));
        } else {
            multiplier = 1;
        }
        event.addModifier((it) -> {
            boolean matches = true;
            if(config.hasPath("on_items")) {
                var items = config.getMaterialList("on_items");
                matches = items.stream().anyMatch((test) -> test.equals(it.getType()));
            }
            if(it.getMaxStackSize() > 1 && matches) {
                it.setAmount((int) (it.getAmount() * multiplier));
            }
            return new DropResult(it, 0);
        });

        return true;
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.EVENT);
    }
}
