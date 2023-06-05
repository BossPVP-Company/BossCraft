package com.bosspvp.api.skills.holder.event;

import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HolderEnableEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final ProvidedHolder holder;
    @Getter
    private final Collection<ProvidedHolder> newHolders;
    public HolderEnableEvent(@NotNull Player who,
                             @NotNull ProvidedHolder holder,
                             @NotNull Collection<ProvidedHolder> newHolders) {
        super(who);
        this.holder = holder;
        this.newHolders = newHolders;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }

}
