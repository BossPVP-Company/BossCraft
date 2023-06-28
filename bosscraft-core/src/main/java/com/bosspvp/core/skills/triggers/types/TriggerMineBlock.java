package com.bosspvp.core.skills.triggers.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TriggerMineBlock extends Trigger {
    public TriggerMineBlock(@NotNull BossPlugin plugin) {
        super(plugin, "mine_block", Set.of(
                TriggerParameter.PLAYER,
                TriggerParameter.BLOCK,
                TriggerParameter.LOCATION,
                TriggerParameter.EVENT,
                TriggerParameter.ITEM)
        );

    }
    @EventHandler(ignoreCancelled = true)
    public void handle(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        this.dispatch(player, TriggerData.builder()
                .player(player)
                .block(block)
                .location(block.getLocation())
                .event(event)
                .item(player.getInventory().getItemInMainHand())
                .build()
        );
    }
}
