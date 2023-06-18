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

public class EffectArmor extends AttributeEffect {
    public EffectArmor(@NotNull BossPlugin plugin) {
        super(plugin,
                "armor",
                Attribute.GENERIC_ARMOR,
                AttributeModifier.Operation.ADD_NUMBER
        );
        setArguments(it->{
            it.require("value","The amount of armor to add/remove.");
        });
    }

    @Override
    protected double getValue(Config config, Player player) {
        return config.getEvaluated("value",
                new PlaceholderContext(player,null, null, new ArrayList<>()));
    }
}
