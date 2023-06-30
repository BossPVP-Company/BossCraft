package com.bosspvp.api.skills.visualeffects;

import org.jetbrains.annotations.NotNull;

public interface VisualEffectTask {







    void start(VisualEffect effect);

    void cancel(VisualEffect effect);

    void finish(VisualEffect effect);

    /**
     * The original effect that created this task
     */
    @NotNull
    VisualEffect getParent();

    /**
     * The unique id of this task
     */
    @NotNull String getId();
}
