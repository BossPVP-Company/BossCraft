package com.bosspvp.api.skills.holder;

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
public class HolderManager {
    private final List<HolderProvider> holderProviders = new ArrayList<>();
    private final List<Consumer<Player>> playerRefreshFunctions = new ArrayList<>();
    //abomination I know
    private final List<BiFunction<ProvidedHolder,Player, Collection<InjectablePlaceholder>>>
            holderPlaceholderProviders = new ArrayList<>();
    @Getter
    private final HashMap<UUID, List<ProvidedEffectBlockList>> previousStates = new HashMap<>();
    private final HashMap<UUID, List<ProvidedEffectBlock>> flattenedPreviousStates = new HashMap<>();
    private final HashMap<UUID, Collection<ProvidedHolder>> previousHolders = new HashMap<>();
    private final Cache<UUID,Collection<ProvidedHolder>> holderCache = Caffeine.newBuilder()
            .expireAfterWrite(4, TimeUnit.SECONDS)
            .build();

    public void registerHolderProvider(HolderProvider provider){
        holderProviders.add(provider);
    }
    public void registerHolderProvider(Function<Player, Collection<ProvidedHolder>> provider){
        registerHolderProvider((HolderProvider) provider::apply);
    }

    public void registerPlayerRefreshFunction(Consumer<Player> function){
        playerRefreshFunctions.add(function);
    }

    public void registerHolderPlaceholderProvider(BiFunction<ProvidedHolder,Player, Collection<InjectablePlaceholder>> provider){
        holderPlaceholderProviders.add(provider);
    }



    public Collection<ProvidedHolder> getPlayerHolders(Player player){
        return holderCache.get(player.getUniqueId(), (key) -> {
            Collection<ProvidedHolder> holders = new ArrayList<>();
            for (HolderProvider provider : holderProviders) {
                holders.addAll(provider.provide(player));
            }
            Bukkit.getPluginManager().callEvent(
                   new HolderProvideEvent(player, holders)
            );

            var old = previousHolders.get(player.getUniqueId());
            if(old == null){
                old = Collections.emptyList();
            }

            var newID = holders.stream().map((holder) -> holder.getHolder().getId()).toList();
            var oldID = old.stream().map((holder) -> holder.getHolder().getId()).toList();

            var added = newID.stream().filter((id) -> !oldID.contains(id)).toList();
            var removed = oldID.stream().filter((id) -> !newID.contains(id)).toList();

            var newByID = holders.stream().collect(
                    Collectors.toMap((holder) -> holder.getHolder().getId(), (holder) -> holder));
            var oldByID = old.stream().collect(
                    Collectors.toMap((holder) -> holder.getHolder().getId(), (holder) -> holder));

            for (var id : added) {
                Bukkit.getPluginManager().callEvent(
                        new HolderEnableEvent(player, newByID.get(id), holders)
                );
            }
            for(var id : removed){
                Bukkit.getPluginManager().callEvent(
                        new HolderDisableEvent(player, oldByID.get(id), old)
                );
            }
            previousHolders.put(player.getUniqueId(), holders);
            return holders;
        });
    }

    public void refreshHolders(@NotNull Player player){
        playerRefreshFunctions.forEach((function) -> function.accept(player));
        updateHolders(player);
        updateEffects(player);
    }
    public void updateHolders(@NotNull Player player){
        holderCache.invalidate(player.getUniqueId());
    }
    public void updateEffects(@NotNull Player player){
        var before = previousStates.get(player.getUniqueId());
        var after = getActiveEffects(player,getPlayerHolders(player));

        List<ProvidedEffectBlock> afterF = new ArrayList<>();
        for(List<ProvidedEffectBlock> l : after.stream().map(ProvidedEffectBlockList::flatten).toList()){
            afterF.addAll(l);
        }
        List<ProvidedEffectBlock> beforeF = new ArrayList<>();
        for(List<ProvidedEffectBlock> l : before.stream().map(ProvidedEffectBlockList::flatten).toList()){
            beforeF.addAll(l);
        }
        previousStates.put(player.getUniqueId(), after);
        flattenedPreviousStates.put(player.getUniqueId(), afterF);

        var added = afterF.stream().filter((it) -> !beforeF.contains(it)).sorted().toList();
        var removed = beforeF.stream().filter((it) -> !afterF.contains(it)).sorted().toList();
        var toReload = afterF.stream().filter((it) -> !added.contains(it)).sorted().toList();

        for(var holder : removed){
            holder.effect().disable(player, holder.holder(),false);
        }
        for(var holder : added){
            holder.effect().enable(player, holder.holder(),false);
        }
        for(var holder : toReload){
            holder.effect().disable(player, holder.holder(), true);
        }
        for(var holder : toReload){
            holder.effect().enable(player, holder.holder(), true);
        }
    }
    public List<ProvidedEffectBlockList> getActiveEffects(@NotNull Player player, @NotNull Collection<ProvidedHolder> holders){
        var blocks = new ArrayList<ProvidedEffectBlockList>();
        for(var holder : holders){
            if(holder.getHolder().getConditionList().areMet(player, holder)){
                blocks.add(new ProvidedEffectBlockList(holder, holder.getActiveEffects(player)));
            }
        }
        return blocks;
    }

    public List<InjectablePlaceholder> generatePlaceholders(ProvidedHolder holder, Player player){
        return holderPlaceholderProviders.stream().flatMap((it) -> it.apply(holder, player).stream()).toList();
    }

}
