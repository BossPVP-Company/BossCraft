package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectVariable;
import com.bosspvp.api.skills.visualeffects.utils.OpenSimplex2S;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Random;

public class StraightLine extends BaseEffect {
    private final long SEED = new Random(System.nanoTime()).nextLong();
    private double time;

    private BaseEffectVariable<Boolean> noiseEnabled = new BaseEffectVariable<>(false, Boolean::parseBoolean);
    private BaseEffectVariable<Boolean> noiseIsAnimated = new BaseEffectVariable<>(false, Boolean::parseBoolean);
    private BaseEffectVariable<Vector>  noiseVector = new BaseEffectVariable<>(new Vector(),
            (it)->{
                String[] split = it.split(";");
                return new Vector(Double.parseDouble(split[0])* MathUtils.degreesToRadians,
                        Double.parseDouble(split[1])*MathUtils.degreesToRadians,
                        Double.parseDouble(split[2])*MathUtils.degreesToRadians);
            });
    private BaseEffectVariable<Double> noiseSpeed = new BaseEffectVariable<>(0.0, Double::parseDouble);


    /**
     * Displays a Straight line
     *
     */
    public StraightLine(VisualEffectsManager effectsManager) {
        super(effectsManager);
        getVariables().put("settings.noise.enabled", noiseEnabled);
        getVariables().put("settings.noise.enable-animation", noiseIsAnimated);
        getVariables().put("settings.noise.vector", noiseVector);
        getVariables().put("settings.noise.speed", noiseSpeed);
    }

    @Override
    public void onRun() {
        if(getTarget()==null) {
            cancel(false);
            return;
        }
        Location location = getOrigin().updateLocation();
        Location target = getTarget().updateLocation();
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        int particles = (int) (length / thickness.getValue());
        float ratio = length / particles;

        Vector v = link.multiply(ratio);
        Location loc = location.clone();
        Location loc1;

        //for faster access in a loop
        boolean noiseEnabled = this.noiseEnabled.getValue();
        boolean noiseIsAnimated = this.noiseIsAnimated.getValue();
        Vector noiseVector = this.noiseVector.getValue();
        double noiseSpeed = this.noiseSpeed.getValue();
        Particle particle = getParticleType().getValue();

        for (int i = 0; i < particles; i++) {
            if(noiseEnabled) {
                float noise = OpenSimplex2S.noise2(SEED, loc.getX() + loc.getY() + loc.getZ(), time);
                loc1 = loc.clone().add(noiseVector.clone().multiply(
                        2 * noise * MathUtils.fastSin(((float) i) / (float) (particles) * Math.PI)
                        )
                );
                displayParticle(particle, loc1);
            }else {
                displayParticle(particle, loc);
            }
            loc.add(v);
            //
        }
        if(noiseIsAnimated) {
            time += noiseSpeed;
        }
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }
}
