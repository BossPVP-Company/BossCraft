package com.bosspvp.api.skills.holder;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.skills.holder.event.HolderDisableEvent;
import com.bosspvp.api.skills.holder.event.HolderEnableEvent;
import com.bosspvp.api.skills.holder.event.HolderProvideEvent;
import com.bosspvp.api.skills.holder.provided.ProvidedEffectBlock;
import com.bosspvp.api.skills.holder.provided.ProvidedEffectBlockList;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Contains all the data and methods related to holders
 */
public interface HolderManager {
    /**
     * Gets the previous state of the player
     * @param player the player to get the previous state of
     * @return the previous state of the player
     */
    List<ProvidedEffectBlockList> getPreviousState(Player player);
    /**
     * Registers a holder provider
     * @param provider the provider to register
     */
    void registerHolderProvider(HolderProvider provider);

    /**
     * Registers a player refresh function
     * @param function the function to register
     */
    void registerPlayerRefreshFunction(Consumer<Player> function);

    /**
     * Registers a holder placeholder provider
     * @param provider the provider to register
     */
    void registerHolderPlaceholderProvider(BiFunction<ProvidedHolder,Player, Collection<InjectablePlaceholder>> provider);

    /**
     * Gets the holders of a player
     * @param player the player to get the holders of
     * @return the holders of the player
     */
    Collection<ProvidedHolder> getPlayerHolders(Player player);

    /**
     * Refreshes the holders of a player
     * @param player the player to refresh the holders of
     */
    void refreshHolders(@NotNull Player player);
    /**
     * Updates the holders of a player
     * @param player the player to update the holders of
     */
    void updateHolders(@NotNull Player player);
    /**
     * Updates the effects of a player
     * @param player the player to update the effects of
     */
    void updateEffects(@NotNull Player player);
    /**
     * Gets the active effects of a player
     * @param player the player
     * @return the effects
     */
    List<ProvidedEffectBlockList> getActiveEffects(@NotNull Player player, @NotNull Collection<ProvidedHolder> holders);

    /**
     * Generates the placeholders for a holder
     * @param holder the holder
     * @param player the player
     * @return the generated placeholders
     */
    List<InjectablePlaceholder> generatePlaceholders(ProvidedHolder holder, Player player);


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    @NotNull
    BossPlugin getPlugin();

}
