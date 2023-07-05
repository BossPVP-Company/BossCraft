package com.bosspvp.api.events.drop;

import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EditableBlockDropEvent extends EditableDropEvent {
    private BlockDropItemEvent event;
    private final List<Function<ItemStack,DropResult>> modifiers;
    public EditableBlockDropEvent(BlockDropItemEvent event) {
        super();
        this.event = event;
        modifiers = new ArrayList<>();
    }

    @Override
    public void addModifier(Function<ItemStack, DropResult> modifier) {
        modifiers.add(modifier);
    }

    @Override
    public List<ItemStack> getOriginalItems() {
        return event.getItems().stream().map(Item::getItemStack).toList();
    }

    @Override
    public List<DropResult> getItems() {
        return getOriginalItems().stream().map(itemStack -> modify(modifiers, itemStack)).toList();
    }

    @Override
    public void removeItem(ItemStack item) {
        event.getItems().removeIf(it->it.getItemStack().equals(item));
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }
}
