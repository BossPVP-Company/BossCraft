package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.events.death.EntityKilledByEntityEvent;
import com.bosspvp.api.events.drop.DropResult;
import com.bosspvp.api.events.drop.EditableEntityDropEvent;
import com.bosspvp.api.misc.drops.DropQueue;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerEntityItemDrop extends Trigger {
    public TriggerEntityItemDrop(@NotNull BossPlugin plugin) {
        super(plugin, "entity_item_drop", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.VICTIM,
                TriggerParameter.EVENT,
                TriggerParameter.LOCATION
        ));
    }
    @EventHandler(ignoreCancelled = true)
    public void handle(EntityKilledByEntityEvent event){
        if(event.getDeathEvent().isCancelled()) return;
        if(!(event.getKiller() instanceof Player player)) return;
        EditableEntityDropEvent editableEvent = new EditableEntityDropEvent(event.getDeathEvent());
        this.dispatch(player,
                TriggerData.builder()
                .player(player)
                .victim(event.getVictim())
                .location(event.getVictim().getLocation())
                .event(editableEvent)
                .value(event.getDrops().stream().filter(
                        it->
                        it.getType() != Material.AIR && it.getAmount() > 0
                        ).mapToInt(ItemStack::getAmount).sum())
                .build());
        int sum = editableEvent.getItems().stream().mapToInt(DropResult::xp).sum();
        event.getDrops().clear();
        event.getDrops().addAll(editableEvent.getItems().stream().map(DropResult::itemStack).toList());
        if(sum > 0){
            DropQueue.create(player)
                    .setLocation(event.getVictim().getLocation())
                    .addExperience(sum)
                    .push();
        }
    }
}
