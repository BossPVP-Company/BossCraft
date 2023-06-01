package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registrable;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class TriggerGroup implements Registrable {
    private BossPlugin plugin;
    private String prefix;


    abstract @Nullable Trigger create(String value);

    @Override
    public void onRegister() {
        BossAPI.getInstance().getCorePlugin().addTaskOnReload(this::afterRegister);
    }

    public void afterRegister(){

    }
    @Override
    public @NotNull String getId() {
        return prefix;
    }
}
