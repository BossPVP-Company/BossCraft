package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IdentifierFactory {
    private final BossPlugin plugin;
    private final UUID uuid;

    public IdentifierFactory(@NotNull BossPlugin plugin,
                             @NotNull UUID uuid){
        this.plugin = plugin;
        this.uuid = uuid;
    }
    public Identifiers makeIdentifiers(int offset){
        return new Identifiers(
                plugin,
                makeUUID(offset),
                makeKey(offset)
        );
    }
    private UUID makeUUID(int offset){
        return UUID.nameUUIDFromBytes((uuid.toString()+offset).getBytes());
    }
    private NamespacedKey makeKey(int offset) {
        return new NamespacedKey(
                plugin.getName().toLowerCase(),
                uuid.hashCode()+"_"+offset
        );
    }
    @Override
    public String toString() {
        return "IdentifierFactory(uuid="+uuid+")";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IdentifierFactory obj)) {
            return false;
        }

        return obj.uuid.equals(uuid);
    }
    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
