package com.bosspvp.api.utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {
    public static void display(@NotNull Particle particle,
                               @NotNull Location center,
                               double offsetX,
                               double offsetY,
                               double offsetZ,
                               float speed,
                               int amount,
                               @Nullable Color color,
                               @Nullable Material material,
                               List<Player> viewers
    ) {

        if(material==null) material = Material.BARRIER;
        if (color == null) color = Color.RED;

        if (particle == Particle.ITEM_CRACK) {
            displayItemCrack(particle, center, offsetX, offsetY, offsetZ, speed, amount, material, viewers);
            return;
        }

        if (particle == Particle.SPELL_MOB_AMBIENT || particle == Particle.SPELL_MOB) {
            displayLegacyColoredParticle(particle, center,speed, color, viewers);
            return;
        }

        Object data = null;
        if (particle == Particle.BLOCK_DUST || particle == Particle.BLOCK_CRACK || particle == Particle.FALLING_DUST) {
            data = material.createBlockData();
        }

        if (particle == Particle.REDSTONE) {

            data = new Particle.DustOptions(color, 1);
        }

        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, data, viewers);
    }

    public static void display(@NotNull Particle particle,
                               @NotNull Location center,
                               double offsetX,
                               double offsetY,
                               double offsetZ,
                               float speed,
                               int amount,
                               @Nullable Color color,
                               @Nullable Material material,
                               double range
    ) {

        List<Player> viewers = new ArrayList<>();
        double squaredRange = range * range;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() != center.getWorld() || player.getLocation().distanceSquared(center) > squaredRange) {
                continue;
            }
            viewers.add(player);
        }
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, color, material, viewers);
    }

    private static void display(@NotNull Particle particle,
                                @NotNull Location center,
                                double offsetX,
                                double offsetY,
                                double offsetZ,
                                float speed,
                                int amount,
                                @Nullable Object data,
                                @NotNull List<Player> viewers) {
        try {
            for (Player player : viewers) {
                player.spawnParticle(particle, center, amount, offsetX, offsetY, offsetZ, speed, data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayItemCrack(@NotNull Particle particle,
                                         @NotNull Location center,
                                         double offsetX,
                                         double offsetY,
                                         double offsetZ,
                                         float speed,
                                         int amount,
                                         @NotNull Material material,
                                         @NotNull List<Player> viewers
    ) {
        if (material == Material.AIR) {
            return;
        }

        ItemStack item = new ItemStack(material);
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, item, viewers);
    }

    private static void displayLegacyColoredParticle(@NotNull Particle particle,
                                                     @NotNull Location center,
                                                     float speed,
                                                     Color color,
                                                     @NotNull List<Player> viewers
    ) {
        int amount = 0;

        if (speed == 0) {
            speed = 1;
        }

        float offsetX = (float) color.getRed() / 255;
        float offsetY = (float) color.getGreen() / 255;
        float offsetZ = (float) color.getBlue() / 255;

        if (offsetX < Float.MIN_NORMAL) {
            offsetX = Float.MIN_NORMAL;
        }

        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, null, viewers);
    }



    private ParticleUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
