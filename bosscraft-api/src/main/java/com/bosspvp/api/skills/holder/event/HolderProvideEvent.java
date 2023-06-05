package com.bosspvp.api.skills.holder.event;

import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class HolderProvideEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private final Collection<ProvidedHolder> holders;
    public HolderProvideEvent(@NotNull Player who,
                              @NotNull Collection<ProvidedHolder> holders) {
        super(who);
        this.holders = holders;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }

}
