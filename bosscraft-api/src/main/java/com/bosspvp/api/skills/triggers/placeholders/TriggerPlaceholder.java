package com.bosspvp.api.skills.triggers.placeholders;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.skills.triggers.TriggerData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


@AllArgsConstructor
public abstract class TriggerPlaceholder implements Listener, Registrable {
    @Getter
    protected final BossPlugin plugin;
    @NotNull @Getter
    private String id;

    @Override
    public void onRegister() {
        plugin.addTaskOnReload(() -> {
                    plugin.getEventManager().unregisterListener(this);
                    plugin.getEventManager().registerListener(this);
                    afterRegister();
                }
        );
    }

    public abstract Collection<InjectablePlaceholder> createPlaceholders(TriggerData data);

    public void afterRegister() {

    }
}
