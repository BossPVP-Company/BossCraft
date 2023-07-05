package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.events.drop.DropResult;
import com.bosspvp.api.events.drop.EditableBlockDropEvent;
import com.bosspvp.api.misc.drops.DropQueue;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerBlockItemDrop extends Trigger {
    public TriggerBlockItemDrop(@NotNull BossPlugin plugin) {
        super(plugin, "block_item_drop", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.BLOCK,
                TriggerParameter.MATERIAL,
                TriggerParameter.EVENT,
                TriggerParameter.LOCATION
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(BlockDropItemEvent event){
        Player player = event.getPlayer();
        var block = event.getBlock();
        if(player.getGameMode() == org.bukkit.GameMode.CREATIVE
                || player.getGameMode() == org.bukkit.GameMode.SPECTATOR) return;
        if(event.getBlockState() instanceof org.bukkit.block.Container) return;

        var originalDrops = event.getItems().stream()
                .map(Item::getItemStack).filter(
                        it-> it.getType() != Material.AIR && it.getAmount() > 0
                ).toList();

        var editableEvent = new EditableBlockDropEvent(event);
        this.dispatch(player,
                TriggerData.builder()
                        .player(player)
                        .block(block)
                        .material(event.getBlockState().getType())
                        .location(block.getLocation())
                        .event(editableEvent)
                        .value(originalDrops.stream().mapToInt(ItemStack::getAmount).sum())
                        .build());
        var newDrops = editableEvent.getItems();
        for(int i = 0; i < event.getItems().size(); i++){
            event.getItems().get(i).setItemStack(newDrops.get(i).itemStack());
        }
        if(newDrops.stream().mapToInt(DropResult::xp).sum() > 0){
            DropQueue.create(player)
                    .setLocation(block.getLocation())
                    .addExperience(newDrops.stream().mapToInt(DropResult::xp).sum())
                    .push();
        }
    }

}
