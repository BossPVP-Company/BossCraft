package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

public class EffectPotionEffect extends Effect<Compilable.NoCompileData> {
    public EffectPotionEffect(@NotNull BossPlugin plugin) {
        super(plugin, "potion_effect");
        setArguments(arguments->{
            arguments.require("effect", "You must specify a valid potion effect! See here: " +
                    "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html");
            arguments.require("level", "You must specify the effect level!");
            arguments.require("duration", "You must specify the duration!");
        });
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.PLAYER);
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        LivingEntity toApply = data.player() != null ? data.player() : data.victim();
        if (toApply == null) return false;
        toApply.addPotionEffect(
                new org.bukkit.potion.PotionEffect(
                        Objects.requireNonNullElse(
                                PotionEffectType.getByName(config.getString("effect").toUpperCase()),
                                PotionEffectType.INCREASE_DAMAGE
                        ),
                        (int) config.getEvaluated("duration", data.toPlaceholderContext(config)),
                        (int) config.getEvaluated("level", data.toPlaceholderContext(config)) - 1,
                        true,
                        true,
                        true
                )
        );
        return true;
    }
}
