package com.bosspvp.api.skills.effects.templates;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.effects.Identifiers;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AttributeEffect extends Effect<Compilable.NoCompileData> {
    private final Attribute attribute;
    private final AttributeModifier.Operation operation;

    public AttributeEffect(@NotNull BossPlugin plugin,
                           @NotNull String id,
                           @NotNull Attribute attribute,
                           @NotNull AttributeModifier.Operation operation) {
        super(plugin,id);
        this.attribute = attribute;
        this.operation = operation;
    }

    protected abstract double getValue(Config config, Player player);
    private void clean(AttributeInstance attributeInstance, String name) {
        for (AttributeModifier modifier : attributeInstance.getModifiers()) {
            if (modifier.getName().equals(getId()) || modifier.getName().equals(name)) {
                attributeInstance.removeModifier(modifier);
            }
        }
    }
    public void constrainAttribute(Player player, double value) {
        // Override this to constrain the attribute value, e.g. to set health below max health.
    }
    @Override
    public void onEnable(Player player,
                         Config config,
                         Identifiers identifiers,
                         ProvidedHolder holder,
                         Compilable.NoCompileData compileData) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }
        //messy I know
        String modifierName = "bosspvp:" + getId() + " - " + identifiers.key().getKey() + " (" + holder.getHolder().getId() + ")";
        clean(attributeInstance, modifierName);
        AttributeModifier modifier = new AttributeModifier(identifiers.uuid(), modifierName, getValue(config, player), operation);
        attributeInstance.removeModifier(modifier);
        attributeInstance.addModifier(modifier);
    }

    @Override
    protected void onDisable(Player player, Identifiers identifiers, ProvidedHolder holder) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }
        String modifierName = "bosspvp:" + getId() + " - " + identifiers.key().getKey() + " (" + holder.getHolder().getId() + ")";
        clean(attributeInstance, modifierName);
        attributeInstance.removeModifier(new AttributeModifier(identifiers.uuid(), modifierName, 0, operation));
        getPlugin().getScheduler().run(() -> constrainAttribute(player, attributeInstance.getValue()));
    }
}
