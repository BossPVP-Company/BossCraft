package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.holder.provided.SimpleProvidedHolder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class EffectAddHolder extends Effect<Holder.Template> {
    private final HashMap<UUID, List<Holder>> holders = new HashMap<>();
    public EffectAddHolder(@NotNull BossPlugin plugin) {
        super(plugin, "add_holder");
        setArguments(arguments -> {
            arguments.require("effects", "You must specify the effects!");
            arguments.require("duration", "You must specify the duration (in ticks)!");
        });
        getPlugin().getSkillsManager().getHolderManager().registerHolderProvider(
                player -> holders.getOrDefault(player.getUniqueId(),new ArrayList<>()).stream()
                        .map(SimpleProvidedHolder::new).collect(Collectors.toSet())
        );
    }


    @Override
    protected boolean onTrigger(Config config, TriggerData data, Holder.Template compileData) {
        var player = data.player();
        if (player == null) return false;
        var duration = config.getEvaluated("duration", data.toPlaceholderContext(config));
        var holder = compileData.toHolder("template_"+UUID.randomUUID());
        holders.computeIfAbsent(player.getUniqueId(), uuid -> List.of());
        holders.get(player.getUniqueId()).add(holder);
        getPlugin().getScheduler().runLater((long) duration, () -> holders.get(player.getUniqueId()).remove(holder));
        return true;
    }

    @Override
    public Holder.Template makeCompileData(Config config, ViolationContext context) {
        var effects = getPlugin().getSkillsManager().getEffectsRegistry().compile(
                config.getSubsectionList("effects"),
                context.with("add_holder effects")
        );
        var conditions = getPlugin().getSkillsManager().getConditionsRegistry().compile(
                config.getSubsectionList("conditions"),
                context.with("add_holder conditions")
        );
        return new Holder.Template(effects, conditions);
    }


    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.PLAYER);
    }
}
