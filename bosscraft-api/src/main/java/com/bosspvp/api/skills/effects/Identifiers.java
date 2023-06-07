package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossPlugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Identifiers(@NotNull BossPlugin plugin,
                          @NotNull UUID uuid,
                          @NotNull NamespacedKey key) {
    public IdentifierFactory makeFactory() {
        return new IdentifierFactory(plugin,uuid);
    }
}
