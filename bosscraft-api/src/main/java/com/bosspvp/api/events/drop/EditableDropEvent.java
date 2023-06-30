package com.bosspvp.api.events.drop;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class EditableDropEvent extends Event implements Cancellable {
    private static HandlerList handlerList = new HandlerList();


    public DropResult modify(Collection<Function<ItemStack,Integer>> modifiers,
                             ItemStack itemStack){
        int xp = 0;
        for(Function<ItemStack,Integer> entry : modifiers){
            xp += entry.apply(itemStack);
        }
        return new DropResult(itemStack,xp);

    }

    abstract void addModifier(Function<ItemStack,Integer> modifier);
    abstract List<ItemStack> getOriginalItems();
    abstract List<DropResult> getItems();
    abstract void removeItem(ItemStack itemStack);


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
