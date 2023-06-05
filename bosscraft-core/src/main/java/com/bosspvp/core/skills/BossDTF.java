package com.bosspvp.core.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.schedule.Scheduler;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.skills.triggers.DispatchedTriggerFactory;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BossDTF implements DispatchedTriggerFactory {

    private HashMap<UUID, List<Integer>> playerTriggers = new HashMap<>();

    @Override @Nullable
    public DispatchedTrigger create(Player player,Trigger trigger, TriggerData data) {
        if (!trigger.isEnabled()) {
            return null;
        }


        int hash =  (trigger.hashCode() << 5) ^ data.hashCode();
        List<Integer> list = playerTriggers.get(player.getUniqueId());
        if(list == null) {
            list = new ArrayList<>();
            list.add(hash);
            playerTriggers.put(player.getUniqueId(), list);
            return new DispatchedTrigger(player, trigger, data);
        }

        if (list.contains(hash)) {
            return null;
        }
        list.add(hash);
        return new DispatchedTrigger(player, trigger, data);
    }

    protected void startTicking(BossPlugin plugin) {
        plugin.getScheduler().runTimer(1, 1, () -> {
            playerTriggers.clear();
        });
    }
}
