package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.skills.triggers.Trigger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class EffectBlock extends ElementLike {
    private final UUID uuid;
    @Getter
    private final Config config;
    @Getter
    private final Chain effects;
    @Getter
    private final Collection<Trigger> triggers;
    @Getter
    private final EffectArgumentList arguments;
    @Getter
    private final ConditionList conditions;
    @Getter
    private final boolean elementOwnChain;
    //@TODO
   /* override val mutators: MutatorList,
    override val filters: FilterList,*/

    @Getter
    private boolean supportDelay;
    @Getter
    private int weight;

    public EffectBlock(@NotNull BossPlugin plugin,
                       @NotNull UUID uuid,
                       @NotNull Config config,
                       @NotNull Chain effects,
                       @NotNull Collection<Trigger> triggers,
                       @NotNull EffectArgumentList arguments,
                       @NotNull ConditionList conditions,
                       boolean elementOwnChain) {
        super(plugin);
        this.uuid = uuid;
        this.config = config;
        this.effects = effects;
        this.triggers = triggers;
        this.arguments = arguments;
        this.conditions = conditions;
        this.elementOwnChain = elementOwnChain;

        supportDelay = true;
        for (ChainElement<?> entry : effects.getList()) {
            if (!entry.isSupportDelay()) {
                supportDelay = false;
                break;
            }
        }
        weight = effects.getWeight();
    }

    public void enable(
            @NotNull Player player,
            @NotNull ProvidedHolder holder,
            boolean isReload){

        effects.getList().forEach(it-> it.enable(player, holder, isReload));
    }

    public void disable(
            @NotNull Player player,
            @NotNull ProvidedHolder holder,
            boolean isReload) {

        effects.getList().forEach(it -> it.disable(player, holder, isReload));

    }

    public void tryTrigger(DispatchedTrigger trigger) {
        if (!triggers.contains(trigger.trigger())) {
            return;
        }
        trigger(trigger);
    }

    @Override
    protected boolean doTrigger(DispatchedTrigger trigger) {
        return effects.trigger(trigger);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EffectBlock effectBlock)) {
            return false;
        }
        return this.uuid.equals(effectBlock.uuid);
    }

    public int hashCode() {
        return Objects.hashCode(this.uuid);
    }
    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }
}
