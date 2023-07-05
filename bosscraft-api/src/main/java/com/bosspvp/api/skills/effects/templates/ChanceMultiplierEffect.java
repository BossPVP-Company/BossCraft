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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChanceMultiplierEffect extends Effect<Compilable.NoCompileData> {
    private HashMap<UUID, List<HashMap<UUID, Supplier<Double>>>> modifiers;
    public ChanceMultiplierEffect(@NotNull BossPlugin plugin, @NotNull String id) {
        super(plugin, id);
        modifiers = new HashMap<>();
        setArguments(it -> {
            it.require("chance", "You must specify the chance!");
        });
    }

    @Override
    protected void onEnable(Player player, Config config, Identifiers identifiers, ProvidedHolder holder, NoCompileData compileData) {
        List<HashMap<UUID, Supplier<Double>>> list = modifiers.get(player.getUniqueId());
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(new HashMap<>() {{
            put(identifiers.uuid(), () -> config.getEvaluated("multiplier",
                    new PlaceholderContext(player, null, null, new ArrayList<>())
            ));
        }});
        modifiers.put(player.getUniqueId(), list);
    }

    @Override
    protected void onDisable(Player player, Identifiers identifiers, ProvidedHolder holder) {
        List<HashMap<UUID, Supplier<Double>>> list = modifiers.get(player.getUniqueId());
        if(list == null) {
            return;
        }
        list.removeIf(it -> it.containsKey(identifiers.uuid()));
        modifiers.put(player.getUniqueId(), list);
    }

    protected boolean passesChance(Player player){
        double chance = 1.0;
        List<HashMap<UUID, Supplier<Double>>> list = modifiers.get(player.getUniqueId());
        if(list == null) {
            return true;
        }
        for(HashMap<UUID, Supplier<Double>> map : list) {
            for(Supplier<Double> supplier : map.values()) {
                chance *= (100 - supplier.get()) / 100;
            }
        }
        return Math.random() > chance;
    }
}
