package com.bosspvp.api.skills.effects.templates;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.effects.Identifiers;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class MultiMultiplierEffect<T> extends Effect<Compilable.NoCompileData> {

    //HashMap<UUID, Supplier<Double> is a Multiplier Modifier
    private final HashMap<UUID, HashMap<UUID, Supplier<Double>>> globalModifiers = new HashMap<>();
    private final HashMap<UUID, HashMap<T, HashMap<UUID, Supplier<Double>>>> modifiers = new HashMap<>();
    public MultiMultiplierEffect(@NotNull BossPlugin plugin, @NotNull String id) {
        super(plugin, id);
        setArguments(arguments ->
                arguments.require("multiplier", "You must specify the multiplier!")
        );
    }
    public abstract String getKey();

    @Override
    protected final void onEnable(Player player, Config config, Identifiers identifiers, ProvidedHolder holder, NoCompileData compileData) {
        if(config.hasPath(getKey())) {
            T element = getElement(config.getString(getKey()));
            if(element == null) return;
            modifiers.computeIfAbsent(player.getUniqueId(),
                    (it)->new HashMap<>()).computeIfAbsent(element,
                    (it)->new HashMap<>()).put(identifiers.uuid(),
                    ()->config.getEvaluated("multiplier", new PlaceholderContext(player))
            );
        } else {
            globalModifiers.computeIfAbsent(player.getUniqueId(),
                    (it)->new HashMap<>()).put(identifiers.uuid(),
                    ()->config.getEvaluated("multiplier", new PlaceholderContext(player))
            );
        }
    }

    @Override
    protected void onDisable(Player player, Identifiers identifiers, ProvidedHolder holder) {
        globalModifiers.computeIfPresent(player.getUniqueId(),
                (uuid, map)->{
                    map.remove(identifiers.uuid());
                    if(map.isEmpty()) return null;
                    return map;
                }
        );
        modifiers.computeIfPresent(player.getUniqueId(),
                (uuid, map)->{
                    map.values().forEach((it)->it.remove(identifiers.uuid()));
                    map.entrySet().removeIf((it)->it.getValue().isEmpty());
                    if(map.isEmpty()) return null;
                    return map;
                }
        );
    }
    protected double getMultiplier(Player player, T element){
        double multiplier = 1.0;
        HashMap<UUID, Supplier<Double>> global = globalModifiers.get(player.getUniqueId());
        if(global != null) {
            for(Supplier<Double> supplier : global.values()) {
                multiplier *= supplier.get();
            }
        }
        HashMap<T, HashMap<UUID, Supplier<Double>>> map = modifiers.get(player.getUniqueId());
        if(map != null) {
            HashMap<UUID, Supplier<Double>> elementMap = map.get(element);
            if(elementMap != null) {
                for(Supplier<Double> supplier : elementMap.values()) {
                    multiplier *= supplier.get();
                }
            }
        }
        return multiplier;
    }

    @Nullable
    public abstract T getElement(String key);
    public abstract Collection<T> getAllElements();
}
