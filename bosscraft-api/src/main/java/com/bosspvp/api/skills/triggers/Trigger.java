package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public abstract class Trigger implements Listener, Registrable {
    @Getter
    protected final BossPlugin plugin;

    @Getter
    private final String id;
    @Getter
    private Set<TriggerParameter> parameters;
    protected final HolderManager holderManager;

    @Getter @Setter
    private boolean enabled;
    public Trigger(@NotNull BossPlugin plugin,
                   @NotNull String id,
                   @NotNull Set<TriggerParameter> parameters){
        this.id = id;
        this.parameters = parameters;
        this.plugin = plugin;
        this.holderManager = plugin.getSkillsManager().getHolderManager();

    }

    protected void dispatch(
            @NotNull Player player,
            @NotNull TriggerData data
    ) {
        dispatch(player,data,null);
    }
    protected void dispatch(
            @NotNull Player player,
            @NotNull TriggerData data,
            @Nullable List<ProvidedHolder> forceHolders
    ) {
        var dispatch = plugin.getSkillsManager().getDispatchedTriggerFactory().create(player, this, data);
        if(dispatch==null) return;
        dispatch.generateTriggerPlaceholders(data);
        //@TODO later, not required rn
        /*val dispatchEvent = TriggerDispatchEvent(player, dispatch)
        Bukkit.getPluginManager().callEvent(dispatchEvent)
        if (dispatchEvent.isCancelled) {
            return
        }*/

        var effects = forceHolders==null?
                holderManager.getPreviousState(player) :
                holderManager.getActiveEffects(player,forceHolders);

        for (var entry : effects) {
            var withHolder = data.copy(entry.holder());
            var dispatchWithHolder = new DispatchedTrigger(player, this, withHolder).inheritPlaceholders(dispatch);

            for (var placeholder : holderManager.generatePlaceholders(entry.holder(),player)) {
                dispatchWithHolder.addPlaceholder(placeholder);
            }

            for (var block : entry.effects()) {
                block.tryTrigger(dispatchWithHolder);
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
