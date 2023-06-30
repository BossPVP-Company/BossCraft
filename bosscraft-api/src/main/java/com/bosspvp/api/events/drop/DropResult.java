package com.bosspvp.api.events.drop;

import org.bukkit.inventory.ItemStack;

public record DropResult(ItemStack itemStack, int xp){}
