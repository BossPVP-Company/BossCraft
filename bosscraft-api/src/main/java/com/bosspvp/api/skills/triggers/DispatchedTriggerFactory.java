package com.bosspvp.api.skills.triggers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface DispatchedTriggerFactory {



    @Nullable DispatchedTrigger create(Player player, Trigger trigger, TriggerData triggerData);
}
