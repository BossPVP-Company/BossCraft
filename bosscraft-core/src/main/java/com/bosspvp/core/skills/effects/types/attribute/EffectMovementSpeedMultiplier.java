package com.bosspvp.core.skills.effects.types.attribute;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.effects.templates.AttributeEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EffectMovementSpeedMultiplier extends AttributeEffect {


    public EffectMovementSpeedMultiplier(@NotNull BossPlugin plugin) {
        super(
                plugin,
                "movement_speed_multiplier",
                Attribute.GENERIC_MOVEMENT_SPEED,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        );
        setArguments(it->{
            it.require("multiplier","You must specify the movement speed multiplier!");
        });
    }

    @Override
    protected double getValue(Config config, Player player) {
        return config.getEvaluated("multiplier",
                new PlaceholderContext(player, null, null, new ArrayList<>())
        ) - 1;
    }
}
