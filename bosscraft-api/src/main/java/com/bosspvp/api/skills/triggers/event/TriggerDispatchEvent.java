package com.bosspvp.api.skills.triggers.event;

import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class TriggerDispatchEvent extends PlayerEvent implements Cancellable {
    @Getter
    private final DispatchedTrigger trigger;

    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;
    public TriggerDispatchEvent(@NotNull Player who,
                                @NotNull DispatchedTrigger trigger) {
        super(who);
        this.trigger = trigger;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
