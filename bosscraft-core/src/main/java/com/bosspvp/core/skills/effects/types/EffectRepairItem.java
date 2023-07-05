package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class EffectRepairItem extends Effect<Compilable.NoCompileData> {
    public EffectRepairItem(@NotNull BossPlugin plugin) {
        super(plugin, "repair_item");
        setArguments(it->{
            it.require("damage", "You must specify the amount of damage to repair!");
        });
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        ItemStack item = data.getFoundItem();
        if(item == null) return false;
        var damage = config.getEvaluated("damage", data.toPlaceholderContext(config));
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return false;
        if(meta.isUnbreakable() || !(meta instanceof Damageable damageable)) return false;
        if(item.getType() == Material.CARVED_PUMPKIN || item.getType() == Material.PLAYER_HEAD) return false;
        damageable.setDamage(damageable.getDamage()-(int)Math.min(damage, damageable.getDamage()));
        item.setItemMeta(meta);
        return true;
    }
}
