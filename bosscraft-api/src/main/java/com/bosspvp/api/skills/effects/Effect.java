package com.bosspvp.api.skills.effects;

import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigArgument;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Effect<T> extends Compilable<T> implements Listener {
    public Effect(@NotNull String id, ConfigArgument.@NotNull Arguments arguments) {
        super(id, arguments);
    }
}
