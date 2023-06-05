package com.bosspvp.api.skills.effects.event;

import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EffectDisableEvent  extends PlayerEvent {
    @Getter
    private final Effect<?> effects;
    @Getter
    private final ProvidedHolder holder;
    private static final HandlerList handlerList = new HandlerList();
    public EffectDisableEvent(@NotNull Player who,
                             @NotNull Effect<?> effects,
                             @NotNull ProvidedHolder holder) {
        super(who);
        this.effects = effects;
        this.holder = holder;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
