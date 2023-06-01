package com.bosspvp.core.skills;

import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.skills.triggers.DispatchedTriggerFactory;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.entity.Player;

public class BossDTF implements DispatchedTriggerFactory {

    private val playerTriggers = listMap<UUID, Int>()

    fun create(player:Player, trigger:Trigger, data:TriggerData): DispatchedTrigger? {
        if (!trigger.isEnabled) {
            return null
        }

        val hash = (trigger.hashCode() shl 5) xor data.hashCode()
        if (hash in playerTriggers[player.uniqueId]) {
            return null
        }

        playerTriggers[player.uniqueId] += hash
        return DispatchedTrigger(player, trigger, data)
    }

    internal fun startTicking() {
        plugin.scheduler.runTimer(1, 1) {
            playerTriggers.clear()
        }
    }
}
