package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.registry.Registrable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class TriggerGroup implements Registrable {
    @Getter
    private String prefix;


    public abstract @Nullable Trigger create(@NotNull String value);

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
