package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EffectRemovePotionEffect extends Effect<Compilable.NoCompileData> {
    public EffectRemovePotionEffect(@NotNull BossPlugin plugin) {
        super(plugin, "remove_potion_effect");
        setArguments(it->{
            it.require("effect", "You must specify a valid potion effect! See here: " +
                    "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html");
            it.require("apply_to_player", "You must specify whether to apply the effect to the player or the victim!");
        });
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.PLAYER);
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        var toApply = config.getBoolOrDefault("apply_to_player",false) ? data.player() : data.victim();
        if(toApply == null) return false;
        getPlugin().getScheduler().runLater(1,()->{
            PotionEffectType effect = PotionEffectType.getByName(config.getString("effect").toUpperCase());
            if(effect == null) return;
            toApply.removePotionEffect(effect);
        });
        return true;
    }
}
