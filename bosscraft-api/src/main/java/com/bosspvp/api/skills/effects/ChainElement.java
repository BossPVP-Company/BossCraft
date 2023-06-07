package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.effects.event.EffectDisableEvent;
import com.bosspvp.api.skills.effects.event.EffectEnableEvent;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ChainElement<T> extends ElementLike implements Compilable.Compiled<T> {
    @Getter
    private Effect<T> effect;
    @Getter
    private Config config;
    @Getter
    private T compileData;
    @Getter
    private EffectArgumentList arguments;
    @Getter
    private ConditionList conditions;
    //@TODO
    /*  override val mutators: MutatorList,
    override val filters: FilterList*/

    private UUID uuid = UUID.randomUUID();
    @Getter
    private boolean supportDelay = effect.isSupportsDelay();
    public ChainElement(@NotNull BossPlugin plugin,
                        @NotNull Effect<T> effect,
                        @NotNull Config config,
                        @NotNull T compileData,
                        @NotNull EffectArgumentList arguments,
                        @NotNull ConditionList conditions){
        super(plugin);
        this.effect = effect;
        this.config = config;
        this.compileData = compileData;
        this.arguments = arguments;
        this.conditions = conditions;
    }

    public void enable(Player player,
                       ProvidedHolder holder,
                       boolean isReload) {

            if (!isReload) {
                Bukkit.getPluginManager().callEvent(new EffectEnableEvent(player, effect, holder));
            }

            effect.enable(player, holder, this, isReload);
    }

    public void disable(Player player,
                        ProvidedHolder holder,
                        boolean isReload) {
            if (!isReload) {
                Bukkit.getPluginManager().callEvent(new EffectDisableEvent(player, effect, holder));
            }

            effect.disable(player, holder, isReload);
    }

    public boolean doTrigger(DispatchedTrigger trigger){
        return effect.trigger(trigger,this);
    }
    public boolean shouldTrigger(DispatchedTrigger trigger){
        return effect.shouldTrigger(trigger,this);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
