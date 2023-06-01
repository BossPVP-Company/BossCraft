package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.EffectList;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.skills.holder.ProvidedHolder;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public record TriggerData(
        @NotNull ProvidedHolder holder,

        @Nullable Player player,
        @Nullable LivingEntity victim,
        @Nullable Block block,
        @Nullable Event event,
        @Nullable Location location,
        @Nullable Projectile projectile,
        @Nullable Vector velocity,
        @Nullable ItemStack item,
        @Nullable String text,
        double value
) {
    TriggerData(){
        this(
                new ProvidedHolder.SimpleProvidedHolder(
                        new Holder.SimpleHolder("blank",
                                new ConditionList(new ArrayList<>()),
                                new EffectList(new ArrayList<>())
                        ),
                        null
                ),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                1.0
            );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                holder,
                player,
                victim,
                block,
                event,
                location,
                projectile,
                velocity,
                item,
                text,
                value
        );
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TriggerData) && obj.hashCode() == this.hashCode();
    }
}
