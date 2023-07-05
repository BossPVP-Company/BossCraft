package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.AdditionalPlayer;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.EffectList;
import com.bosspvp.api.skills.holder.SimpleHolder;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.bosspvp.api.skills.holder.provided.SimpleProvidedHolder;
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
import java.util.List;
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
                new SimpleProvidedHolder(
                        new SimpleHolder("blank",
                                new ConditionList(new ArrayList<>()),
                                new EffectList(new ArrayList<>())
                        )
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
    public ItemStack getFoundItem(){
        if(holder.getProvider() instanceof ItemStack out){
            return out;
        }
        return item;
    }
    public PlaceholderContext toPlaceholderContext(@NotNull Config config) {
        List<AdditionalPlayer> additionalPlayers = new ArrayList<>();
        if(victim()!=null && victim() instanceof Player victimP){
            additionalPlayers.add(new AdditionalPlayer(victimP,"victim"));
        }

        return new PlaceholderContext(player(),
                (ItemStack) holder().getProvider(),
                config,
                additionalPlayers
        );
    }

    public TriggerData.Builder copyToBuilder(){
        //copy all
        return new Builder()
                .holder(holder())
                .player(player())
                .victim(victim())
                .block(block())
                .event(event())
                .item(item())
                .location(location())
                .projectile(projectile())
                .text(text())
                .value(value())
                .velocity(velocity());

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


    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private ProvidedHolder holder = new SimpleProvidedHolder(
                new SimpleHolder("blank",
                        new ConditionList(new ArrayList<>()),
                        new EffectList(new ArrayList<>())
                )
        );

        private Player player;
        private LivingEntity victim;
        private Block block;
        private Event event;
        private Location location;
        private Projectile projectile;
        private Vector velocity;
        private ItemStack item;
        private String text;
        private double value = 1.0;

        public Builder holder(ProvidedHolder holder){
            this.holder = holder;
            return this;
        }
        public Builder player(Player player){
            this.player = player;
            return this;
        }
        public Builder victim(LivingEntity victim){
            this.victim = victim;
            return this;
        }
        public Builder block(Block block){
            this.block = block;
            return this;
        }
        public Builder event(Event event){
            this.event = event;
            return this;
        }
        public Builder location(Location location){
            this.location = location;
            return this;
        }
        public Builder projectile(Projectile projectile){
            this.projectile = projectile;
            return this;
        }
        public Builder velocity(Vector velocity){
            this.velocity = velocity;
            return this;
        }
        public Builder item(ItemStack item){
            this.item = item;
            return this;
        }
        public Builder text(String text){
            this.text = text;
            return this;
        }
        public Builder value(double value){
            this.value = value;
            return this;
        }
        public TriggerData build(){
            return new TriggerData(
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
    }
}
