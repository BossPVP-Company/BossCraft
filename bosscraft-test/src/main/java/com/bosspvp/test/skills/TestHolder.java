package com.bosspvp.test.skills;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.EffectList;
import com.bosspvp.api.skills.holder.Holder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TestHolder implements Holder {
    private final BossPlugin plugin;
    private final Player player;
    @Getter
    private final String id;
    @Getter
    private final EffectList effectList;
    @Getter
    private final ConditionList conditionList;

    public TestHolder(BossPlugin plugin, Player player, EffectList effectList, ConditionList conditionList) {
        this.plugin = plugin;
        this.effectList = effectList;
        this.conditionList = conditionList;
        this.player = player;
        this.id = player.getName();
    }

}
