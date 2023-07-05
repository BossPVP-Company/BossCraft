package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectBuilder;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SingleParticle extends BaseEffect {
    public SingleParticle(@NotNull VisualEffectsManager effectsManager) {
        super(effectsManager);
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
