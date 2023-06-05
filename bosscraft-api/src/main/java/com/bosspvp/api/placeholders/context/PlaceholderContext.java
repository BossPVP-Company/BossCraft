package com.bosspvp.api.placeholders.context;

import com.bosspvp.api.placeholders.AdditionalPlayer;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.InjectablePlaceholderList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlaceholderContext {

    /**
     * The player.
     */
    @Nullable
    private final Player player;

    /**
     * The ItemStack.
     */
    @Nullable
    private final ItemStack itemStack;

    /**
     * The PlaceholderInjectable context.
     */
    @NotNull
    private final InjectablePlaceholderList injectableContext;

    /**
     * The additional players.
     */
    @NotNull
    private final Collection<AdditionalPlayer> additionalPlayers;

    /**
     * Create an empty PlaceholderContext.
     */
    public PlaceholderContext() {
        this(null, null, null, Collections.emptyList());
    }

    /**
     * Create a PlaceholderContext for a player.
     *
     * @param player The player.
     */
    public PlaceholderContext(@Nullable final Player player) {
        this(player, null, null, Collections.emptyList());
    }

    /**
     * Constructs a new PlaceholderContext with the given parameters.
     *
     * @param player            The player.
     * @param itemStack         The ItemStack.
     * @param injectableContext The PlaceholderInjectable parseContext.
     * @param additionalPlayers The additional players.
     */
    public PlaceholderContext(@Nullable final Player player,
                              @Nullable final ItemStack itemStack,
                              @Nullable final InjectablePlaceholderList injectableContext,
                              @NotNull final Collection<AdditionalPlayer> additionalPlayers) {
        this.player = player;
        this.itemStack = itemStack;
        this.injectableContext = Objects.requireNonNullElse(injectableContext, EMPTY_INJECTABLE);
        this.additionalPlayers = additionalPlayers;
    }

    /**
     * Get the player.
     *
     * @return The player.
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the ItemStack.
     *
     * @return The ItemStack.
     */
    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Get the PlaceholderInjectable context.
     *
     * @return The PlaceholderInjectable context.
     */
    @NotNull
    public InjectablePlaceholderList getInjectableContext() {
        return injectableContext;
    }

    /**
     * Get the additional players.
     *
     * @return The additional players.
     */
    @NotNull
    public Collection<AdditionalPlayer> getAdditionalPlayers() {
        return additionalPlayers;
    }



    /**
     * Copy with a player.
     *
     * @param player The player.
     * @return The new context.
     */
    public PlaceholderContext copyWithPlayer(@Nullable final Player player) {
        return new PlaceholderContext(
                player,
                this.getItemStack(),
                this.getInjectableContext(),
                this.getAdditionalPlayers()
        );
    }

    /**
     * Copy with an item.
     *
     * @param itemStack The ItemStack.
     * @return The new context.
     */
    public PlaceholderContext copyWithItem(@Nullable final ItemStack itemStack) {
        return new PlaceholderContext(
                this.getPlayer(),
                itemStack,
                this.getInjectableContext(),
                this.getAdditionalPlayers()
        );
    }

    /**
     * Copy with an extra injectable context.
     *
     * @param injectableContext The injectable context to add.
     * @return The new context.
     */
    public PlaceholderContext withInjectableContext(@NotNull final InjectablePlaceholderList injectableContext) {
        return new PlaceholderContext(
                this.getPlayer(),
                this.getItemStack(),
                new MergedInjectableContext(this.getInjectableContext(), injectableContext),
                this.getAdditionalPlayers()
        );
    }

    public PlaceholderContext copy(ItemStack item) {
        return new PlaceholderContext(
                this.getPlayer(),
                item,
                this.injectableContext,
                this.additionalPlayers
        );
    }
    @Override
    public String toString() {
        return "PlaceholderContext{" +
                "player=" + player +
                ", itemStack=" + itemStack +
                ", injectableContext=" + injectableContext +
                ", additionalPlayers=" + additionalPlayers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PlaceholderContext that)) {
            return false;
        }

        return Objects.equals(
                getPlayer(), that.getPlayer())
                && Objects.equals(getItemStack(), that.getItemStack())
                && getInjectableContext().equals(that.getInjectableContext())
                && getAdditionalPlayers().equals(that.getAdditionalPlayers()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer(), getItemStack(), getInjectableContext(), getAdditionalPlayers());
    }

    /**
     * Create PlaceholderContext of a PlaceholderInjectable parseContext.
     *
     * @param injectableContext The PlaceholderInjectable parseContext.
     * @return The context.
     */
    public static PlaceholderContext of(@NotNull final InjectablePlaceholderList injectableContext) {
        return new PlaceholderContext(
                null,
                null,
                injectableContext,
                Collections.emptyList()
        );
    }

    private static final InjectablePlaceholderList EMPTY_INJECTABLE = new InjectablePlaceholderList() {
        @Override
        public void addInjectablePlaceholder(@NotNull final Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
            // Do nothing.
        }

        @Override
        public void clearInjectedPlaceholders() {
            // Do nothing.
        }

        @Override
        public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
            return Collections.emptyList();
        }
    };
    public static final PlaceholderContext EMPTY = new PlaceholderContext(
            null,
            null,
            null,
            Collections.emptyList()
    );
}
