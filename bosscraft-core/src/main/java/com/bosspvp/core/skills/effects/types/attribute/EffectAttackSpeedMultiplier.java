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

public class EffectAttackSpeedMultiplier extends AttributeEffect {
    public EffectAttackSpeedMultiplier(@NotNull BossPlugin plugin) {
        super(
                plugin,
                "attack_speed_multiplier",
                Attribute.GENERIC_ATTACK_SPEED,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
        );
        setArguments(it->{
            it.require("value","The amount of attack speed to add/remove.");
        });
    }

    @Override
    protected double getValue(Config config,Player player) {
        //config.getDoubleFromExpression("multiplier", player) - 1
        return config.getEvaluated("value", new PlaceholderContext(player,null, null, new ArrayList<>()));
    }
}
