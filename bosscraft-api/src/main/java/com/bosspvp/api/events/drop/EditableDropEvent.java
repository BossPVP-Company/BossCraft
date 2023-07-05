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


    public DropResult modify(Collection<Function<ItemStack,DropResult>> modifiers,
                             ItemStack itemStack){
        int xp = 0;
        for(Function<ItemStack,DropResult> entry : modifiers){
            xp += entry.apply(itemStack).xp();
        }
        return new DropResult(itemStack,xp);

    }

    public abstract void addModifier(Function<ItemStack,DropResult> modifier);
    public abstract List<ItemStack> getOriginalItems();
    public abstract List<DropResult> getItems();
    public abstract void removeItem(ItemStack itemStack);


    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }
}
