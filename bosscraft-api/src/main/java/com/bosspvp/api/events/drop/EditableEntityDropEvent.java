package com.bosspvp.api.events.drop;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EditableEntityDropEvent extends EditableDropEvent {
    private EntityDeathEvent event;
    private final List<Function<ItemStack,DropResult>> modifiers;
    //private val modifiers = mutableListOf<DropModifier>()
    public EditableEntityDropEvent(EntityDeathEvent event) {
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
        return event.getDrops();
    }

    @Override
    public List<DropResult> getItems() {
        return getOriginalItems().stream().map(itemStack -> modify(modifiers, itemStack)).toList();
    }

    @Override
    public void removeItem(ItemStack item) {
        event.getDrops().remove(item);
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
