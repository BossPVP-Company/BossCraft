package com.bosspvp.core.misc;

import com.bosspvp.api.misc.drops.DropQueue;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BossDropQueue implements DropQueue {
    private Player player;
    private List<ItemStack> items;
    private int experience;
    private Location location;
    private boolean hasTelekinesis;
    public BossDropQueue(Player player) {
        this.player = player;
        this.items = new ArrayList<>();
        this.experience = 0;
        this.location = player.getLocation();
        this.hasTelekinesis = false;
    }

    @Override
    public DropQueue addItem(@NotNull ItemStack item) {
        items.add(item);
        return this;
    }

    @Override
    public DropQueue addItems(@NotNull Collection<ItemStack> items) {
        this.items.addAll(items);
        return this;
    }

    @Override
    public DropQueue addExperience(int amount) {
        experience += amount;
        return this;
    }

    @Override
    public DropQueue setLocation(@NotNull Location location) {
        this.location = location;
        return this;
    }

    @Override
    public DropQueue forceTelekinesis() {
        hasTelekinesis = true;
        return this;
    }

    @Override
    public void push() {
        var world = location.getWorld();
        location = location.add(0.5, 0.5, 0.5);
        items.removeIf(itemStack -> itemStack.getType().isAir());
        if (items.isEmpty()) {
            return;
        }
        if (hasTelekinesis) {
            var leftover = player.getInventory().addItem(items.toArray(new ItemStack[0]));
            for (var drop : leftover.values()) {
                world.dropItem(location, drop).setVelocity(new Vector());
            }
            if (experience > 0) {
                var orb = (ExperienceOrb) world.spawnEntity(player.getLocation().add(0, 0.2, 0), EntityType.EXPERIENCE_ORB);
                orb.setVelocity(new Vector(0, 0, 0));
                orb.setExperience(experience);
            }
        } else {
            for (var drop : items) {
                world.dropItem(location, drop).setVelocity(new Vector());
            }
            if (experience > 0) {
                var orb = (ExperienceOrb) world.spawnEntity(location, EntityType.EXPERIENCE_ORB);
                orb.setVelocity(new Vector(0, 0, 0));
                orb.setExperience(experience);
            }
        }
    }

}
