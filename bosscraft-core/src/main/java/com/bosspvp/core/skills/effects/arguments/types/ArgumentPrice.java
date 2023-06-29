package com.bosspvp.core.skills.effects.arguments.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.misc.ecomomy.Prices;
import com.bosspvp.api.misc.sound.PlayableSound;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigurableElement;
import com.bosspvp.api.skills.effects.arguments.EffectArgument;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

//@TODO
public class ArgumentPrice extends EffectArgument<Compilable.NoCompileData> {
    public ArgumentPrice(@NotNull BossPlugin plugin) {
        super(plugin, "price");
    }

    @Override
    public boolean isMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        Config section = element.getConfig().getSubsection("price");

        var price = Prices.create(
                section.getString("value"),
                section.getString("type"),
                trigger.data().toPlaceholderContext(section)
        );
        return price.canAfford(trigger.player());
    }

    @Override
    public void ifMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        Config section = element.getConfig().getSubsection("price");
        var price = Prices.create(
                section.getString("value"),
                section.getString("type"),
                trigger.data().toPlaceholderContext(section)
        );
        price.pay(trigger.player());
    }

    @Override
    public void ifNotMet(@NotNull ConfigurableElement element, @NotNull DispatchedTrigger trigger, @NotNull NoCompileData compileData) {
        Config configYml = getPlugin().getConfigYml().asConfig();
        if(!configYml.getBool("cannot-afford-price.message-enabled")) return;
        Config langYml = getPlugin().getLangYml().asConfig();

        Config section = element.getConfig().getSubsection("price");
        var price = Prices.create(
                section.getString("value"),
                section.getString("type"),
                trigger.data().toPlaceholderContext(section)
        );
        var display = section.getString("price.display")
                .replace("%value%", String.valueOf(price.getValue(trigger.player())));
        var message = langYml.getString("messages.cannot-afford-price");
        message = message.replace("%price%", display);
        if(configYml.getBool("cannot-afford-price.in-actionbar")) {
            trigger.player().sendActionBar(message);
        } else {
            trigger.player().sendMessage(message);
        }
        if(configYml.getBool("cannot-afford-price.sound.enabled")) {
            trigger.player().playSound(trigger.player().getLocation(),
                    Sound.valueOf(configYml.getString("cannot-afford-price.sound.sound").toUpperCase()),
                    1.0f,
                    (float) configYml.getDouble("cannot-afford-price.sound.pitch")
            );
        }
    }
}
