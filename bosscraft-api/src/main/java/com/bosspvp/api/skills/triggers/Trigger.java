package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registrable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class Trigger implements Listener, Registrable {
    @Getter
    private final BossPlugin plugin;

    @Getter
    private final String id;
    private Set<TriggerParameter> parameters;

    @Getter @Setter
    private boolean enabled;
    public Trigger(@NotNull BossPlugin plugin,
                   @NotNull String id,
                   @NotNull Set<TriggerParameter> parameters){
        this.plugin = plugin;
        this.id = id;
        this.parameters = parameters;
    }

    protected void dispatch(
            @NotNull Player player,
            @NotNull TriggerData data,
            @Nullable Collection<ProvidedHolder> forceHolders
    ) {
        val dispatch = plugin.dispatchedTriggerFactory.create(player, this, data) ?: return
                dispatch.generatePlaceholders(data)

        val dispatchEvent = TriggerDispatchEvent(player, dispatch)
        Bukkit.getPluginManager().callEvent(dispatchEvent)
        if (dispatchEvent.isCancelled) {
            return
        }

        val effects = forceHolders?.getProvidedActiveEffects(player) ?: player.providedActiveEffects

        for ((holder, blocks) in effects) {
            val withHolder = data.copy(holder = holder)
            val dispatchWithHolder = DispatchedTrigger(player, this, withHolder).inheritPlaceholders(dispatch)

            for (placeholder in holder.generatePlaceholders(player)) {
                dispatchWithHolder.addPlaceholder(placeholder)
            }

            for (block in blocks) {
                block.tryTrigger(dispatchWithHolder)
            }
        }
    }

    @Override
    public final void onRegister() {
        plugin.getEventManager().unregisterListener(this);
        plugin.getEventManager().registerListener(this);
        afterRegister();
    }

    /**
     * Called after register
     * <p></p>
     * override to add implementation
     */
    protected void afterRegister(){

    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Trigger trigger) && trigger.id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
