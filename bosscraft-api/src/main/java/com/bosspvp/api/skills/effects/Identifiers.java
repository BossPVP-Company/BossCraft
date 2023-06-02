package com.bosspvp.api.skills.effects;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record Identifiers(@NotNull UUID uuid,
                          @NotNull NamespacedKey key) {
    public IdentifierFactory makeFactory() {
        return new IdentifierFactory(uuid);
    }
}
