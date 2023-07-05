package com.bosspvp.api.events.death;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
public class EntityKilledByEntityEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter @NotNull
    private final LivingEntity victim;

    @Getter @NotNull
    private final Entity killer;

    @Getter @Nullable
    private final Projectile projectile;

    @Getter @NotNull
    private final EntityDeathEvent deathEvent;

    @Getter @NotNull
    private final List<ItemStack> drops;

    @Getter
    private final int experience;

    /**
     *
     *
     * @param victim     The killed entity
     * @param killer     The killer
     * @param projectile The projectile that killed the entity.
     * @param drops      The item drops
     * @param experience The amount of xp to drop
     * @param deathEvent The associated {@link EntityDeathEvent}
     */
    public EntityKilledByEntityEvent(@NotNull final LivingEntity victim,
                                    @NotNull final Entity killer,
                                    @Nullable final Projectile projectile,
                                    @NotNull final List<ItemStack> drops,
                                    final int experience,
                                    @NotNull final EntityDeathEvent deathEvent) {
        this.victim = victim;
        this.killer = killer;
        this.projectile = projectile;
        this.drops = drops;
        this.experience = experience;
        this.deathEvent = deathEvent;
    }

    /**
     * Internal bukkit.
     *
     * @return Get the handlers.
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Internal bukkit.
     *
     * @return The handlers.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


    public static Builder builder() {
        return new Builder();
    }
    //builder class
    public static class Builder{
        private LivingEntity victim;
        private Entity killer;
        private Projectile projectile;
        private List<ItemStack> drops;
        private int experience;
        private EntityDeathEvent deathEvent;

        public Builder victim(LivingEntity victim) {
            this.victim = victim;
            return this;
        }

        public Builder killer(Entity killer) {
            this.killer = killer;
            return this;
        }

        public Builder projectile(Projectile projectile) {
            this.projectile = projectile;
            return this;
        }

        public Builder drops(List<ItemStack> drops) {
            this.drops = drops;
            return this;
        }

        public Builder experience(int experience) {
            this.experience = experience;
            return this;
        }

        public Builder deathEvent(EntityDeathEvent deathEvent) {
            this.deathEvent = deathEvent;
            return this;
        }

        public EntityKilledByEntityEvent build() {
            return new EntityKilledByEntityEvent(victim, killer, projectile, drops, experience, deathEvent);
        }
    }
}
