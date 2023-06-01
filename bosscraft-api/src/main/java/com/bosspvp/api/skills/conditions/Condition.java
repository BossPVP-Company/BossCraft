package com.bosspvp.api.skills.conditions;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.holder.ProvidedHolder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class Condition<T> extends Compilable<T> implements Listener {
    @Getter
    private final BossPlugin plugin;

    public Condition(@NotNull BossPlugin plugin,
                     @NotNull String id){
        super(id);
        this.plugin = plugin;
    }
    public boolean isMet(
            Player player,
            Config config,
            T compileData
    ) {
        return true;
    }
    public boolean isMet(
            Player player,
            Config config,
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
