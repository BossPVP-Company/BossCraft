package com.bosspvp.api.skills.effects.templates;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.effects.Identifiers;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class MultiplierEffect extends Effect<Compilable.NoCompileData> {
    private HashMap<UUID, HashMap<UUID, Supplier<Double>>> modifiers = new HashMap<>();
    public MultiplierEffect(BossPlugin plugin, String id) {
        super(plugin, id);
        setArguments(it -> {
            it.require("multiplier", "You must specify the multiplier!");
        });
    }

    @Override
    protected void onEnable(Player player, Config config, Identifiers identifiers, ProvidedHolder holder, NoCompileData compileData) {
        HashMap<UUID, Supplier<Double>> map = modifiers.get(player.getUniqueId());
        if(map == null) {
            map = new HashMap<>();
        }
        map.put(identifiers.uuid(),()->{
            return config.getEvaluated("multiplier", new PlaceholderContext(player,null,null,new ArrayList<>()));
        });
        modifiers.put(player.getUniqueId(), map);
    }

    @Override
    protected void onDisable(Player player, Identifiers identifiers, ProvidedHolder holder) {
        HashMap<UUID, Supplier<Double>> map = modifiers.get(player.getUniqueId());
        if(map == null) {
            return;
        }
        map.remove(identifiers.uuid());
        modifiers.put(player.getUniqueId(), map);
    }

    protected double getMultiplier(Player player) {
        double multiplier = 1.0;
        HashMap<UUID, Supplier<Double>> map = modifiers.get(player.getUniqueId());
        if(map == null) {
            return multiplier;
        }
        for(Supplier<Double> supplier : map.values()) {
            multiplier *= supplier.get();
        }
        return multiplier;
    }
}
