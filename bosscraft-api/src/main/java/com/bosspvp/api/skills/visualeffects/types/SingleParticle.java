package com.bosspvp.api.skills.visualeffects.types;

import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectsRegistry;
import com.bosspvp.api.skills.visualeffects.impl.BaseEffect;
import org.jetbrains.annotations.NotNull;

public class SingleParticle extends BaseEffect {
    public SingleParticle(@NotNull VisualEffectsRegistry effectsManager, @NotNull VisualEffectLocation origin, long period, int iterations) {
        super(effectsManager, origin,period,iterations);
    }

    @Override
    protected void onRun() {
        getOrigin().updateLocation();
        displayParticle(getParticleType().getValue(),getOrigin().getCurrentLocation());
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }
}
