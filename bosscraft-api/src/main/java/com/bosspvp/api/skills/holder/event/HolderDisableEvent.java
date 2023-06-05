package com.bosspvp.api.skills.holder.event;

import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HolderDisableEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final ProvidedHolder holder;
    @Getter
    private final Collection<ProvidedHolder> previousHolders;
    public HolderDisableEvent(@NotNull Player who,
                              @NotNull ProvidedHolder holder,
                              @NotNull Collection<ProvidedHolder> previousHolders) {
        super(who);
        this.holder = holder;
        this.previousHolders = previousHolders;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
