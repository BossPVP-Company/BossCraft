package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EffectDropRandomItem extends Effect<List<ItemStack>> {
    public EffectDropRandomItem(@NotNull BossPlugin plugin) {
        super(plugin, "drop_random_item");
        setArguments(arguments ->
                arguments.require(List.of("items", "item"),
                        "You must specify the list of items to choose from!"
                )
        );
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, List<ItemStack> compileData) {
        var location = data.location();
        if(location == null) return false;
        var item = compileData.get((int) (Math.random() * compileData.size()));
        location.getWorld().dropItem(location, item);
        return true;
    }

    @Override
    public List<ItemStack> makeCompileData(Config config, ViolationContext context) throws Exception {
        return config.getStringList("items")
                .stream()
                .map(Material::matchMaterial)
                .filter(Objects::nonNull)
                .map(item -> new ItemStack(item, 1))
                .toList();
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.LOCATION);
    }
}
