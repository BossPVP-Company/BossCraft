package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.BossConfig;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigArgument;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.skills.holder.ProvidedHolder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class Condition<T> extends Compilable<T> implements Listener {
    @Getter
    private final BossPlugin plugin;

    public Condition(@NotNull BossPlugin plugin,
                     @NotNull String id,
                     @NotNull ConfigArgument.Arguments arguments){
        super(id,arguments);
        this.plugin = plugin;
    }
    public boolean isMet(
            Player player,
            BossConfig config,
            T compileData
    ) {
        return true;
    }
    public boolean isMet(
            Player player,
            BossConfig config,
            ProvidedHolder holder,
            T compileData
    ) {
        return true;
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
}
