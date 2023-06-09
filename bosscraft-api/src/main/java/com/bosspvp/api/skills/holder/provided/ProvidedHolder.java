package com.bosspvp.api.skills.holder.provided;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.effects.EffectBlock;
import com.bosspvp.api.skills.holder.Holder;
import com.bosspvp.api.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProvidedHolder {
    @NotNull Holder getHolder();

    @Nullable Object getProvider();

    default Set<EffectBlock> getActiveEffects(@NotNull Player player) {
        return getHolder().getEffectList().getList().stream()
                .filter(effectBlock -> effectBlock.getConditions().areMet(player, this))
                .collect(Collectors.toSet());
    }

}
