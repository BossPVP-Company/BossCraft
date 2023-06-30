package com.bosspvp.core.skills.effects.arguments.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.effects.arguments.EffectArgument;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.utils.StringUtils;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ArgumentCooldown extends EffectArgument<Compilable.NoCompileData> {
    private final HashMap<UUID, HashMap<UUID, Long>> cooldownTracker = new HashMap<>();

    public ArgumentCooldown(@NotNull BossPlugin plugin) {
        super(plugin, "cooldown");
    }

    @Override
    public boolean isMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        return super.isMet(element, trigger, compileData);
    }

    private int getCooldown(ConfigurableElement element, DispatchedTrigger trigger) {
        HashMap<UUID, Long> effectEndTimes = cooldownTracker.get(element.getUUID());
        if (effectEndTimes == null) return 0;
        Long endTime = effectEndTimes.get(trigger.player().getUniqueId());
        if (endTime == null) return 0;
        long msLeft = endTime - System.currentTimeMillis();
        long secondsLeft = (long) Math.ceil(msLeft / 1000.0);
        return (int) secondsLeft;
    }

    @Override
    public void ifMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        HashMap<UUID, Long> effectEndTimes = cooldownTracker.get(element.getUUID());
        if (effectEndTimes == null) effectEndTimes = new HashMap<>();
        effectEndTimes.put(trigger.player().getUniqueId(), System.currentTimeMillis() +
                (long) (element.getConfig().getEvaluated("cooldown",
                        trigger.data().toPlaceholderContext(element.getConfig())) * 1000L));
        cooldownTracker.put(element.getUUID(), effectEndTimes);
    }

    @Override
    public void ifNotMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        Config configYml = getPlugin().getConfigYml().asConfig();
        Config langYml = getPlugin().getLangYml().asConfig();
        if (!configYml.getBool("send_cooldown_message")) return;
        if (!configYml.getBool("cannot-afford-type.message-enabled")) return;
        int cooldown = getCooldown(element, trigger);
        String message = Objects.requireNonNullElseGet(
                        element.getConfig().getFormattedStringOrNull(
                                "cooldown_message",
                                trigger.data().toPlaceholderContext(element.getConfig())),
                        () -> langYml.getFormattedString("messages.on-cooldown")
                ).replace("%seconds%", String.valueOf(cooldown));
        if (configYml.getBool("cooldown.in-actionbar")) {
            trigger.player().sendActionBar(StringUtils.toComponent(message));
        } else {
            trigger.player().sendMessage(message);
        }
        if (configYml.getBool("cooldown.sound.enabled")) {
            trigger.player().playSound(
                    trigger.player().getLocation(),
                    Sound.valueOf(configYml.getString("cooldown.sound.sound").toUpperCase()),
                    1.0f,
                    (float) configYml.getDouble("cooldown.sound.pitch")
            );
        }

    }
}
