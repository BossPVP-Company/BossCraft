package com.bosspvp.api.skills.visualeffects.types;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectsRegistry;
import com.bosspvp.api.skills.visualeffects.impl.BaseEffect;
import com.bosspvp.api.skills.visualeffects.impl.BaseEffectVariable;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DynamicCircle extends BaseEffect {

    private BaseEffectVariable<Double> radius = new BaseEffectVariable<>(0.5,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );

    private  BaseEffectVariable<Double> radiusAdd  = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private  BaseEffectVariable<Double> radiusMultiply  = new BaseEffectVariable<>(1.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );

    private  BaseEffectVariable<Double> YAdd  = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Vector> rotation = new BaseEffectVariable<>(new Vector(), (it)->{
        String[] split = it.split(";");
        return new Vector(Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2]));
    });
    private double currentY = 0;

    private double noiseAmplitudeAdd = 0;
    private double noiseAmplitudeMultiply = 1;
    private double noisePointMultiplier = 0.5;
    private double noiseSpeed = 0.05;



    private boolean noise = false;
    private double timer = 0;

    /**
     * Displays a Dynamic Circle
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     *                       <p><p/>
     * @param period         <i>Sets update rate (in Ticks)<i/>
     *                       <p><p/>
     * @param iterations     <i>Sets amount of run() calls.<i/>
     *                       <p></p>
     *                       <i>'-1' -> uses runTask() than runTaskTimer()<i/>
     *                       <p></p>
     *                       <i>'-2' -> infinite, can be stopped only manually.<i/>
     * @apiNote parameters examples:
     * <ul>
     *     <li>Explosion: <b>thickness:</b> 0.1; <b>radius:</b> 0.3; <b>radiusMultiplier:</b> 1.3; <b>iterations:</b> 10</li>
     *     <li>Cool effect: <b>thickness:</b> 0.1; <b>radius:</b> 0.3; <b>radiusMultiplier:</b> 1.15; <b>iterations:</b> 30; <b>noisePointMultiplier:</b> 0.2; <b>repeats:</b> -1; <b>repeatDelay:</b> -1</li>
     * </ul>
     */
    public DynamicCircle(@NotNull VisualEffectsRegistry effectsManager,
                         @NotNull VisualEffectLocation origin,
                         long period,
                         int iterations) {

        super(effectsManager, origin, period, iterations);
        getVariables().put("settings.radius.value", radius);
        getVariables().put("settings.radius.add", radiusAdd);
        getVariables().put("settings.radius.multiply", radiusMultiply);
        getVariables().put("settings.y_add", YAdd);
        getVariables().put("settings.rotation", rotation);
    }

    @Override
    public void onRun() {
        Location location = getOrigin().updateLocation();

        currentY += YAdd.getValue();
        location.add(0, currentY, 0);

        radius.setValue((radius.getValue() * radiusMultiply.getValue()) + radiusAdd.getValue());

        double radius = this.radius.getValue();
        Vector rotation = this.rotation.getValue();
        boolean rotationEnabled = rotation.getX() != 0 || rotation.getY() != 0 || rotation.getZ() != 0;

        int particles = (int) (Math.PI * radius * 2 / thickness.getValue());
        double step = Math.PI * 2 / particles;
        Vector vector = new Vector();
        for (int i = 0; i < particles; i++) {
            vector.setX(radius * MathUtils.fastSin(step * i));
            vector.setZ(radius * MathUtils.fastCos(step * i));
            //set to 0, to make rotation weird:D
            vector.setY(1);
            if (rotationEnabled) {
                vector.rotateAroundX(rotation.getX() * MathUtils.degreesToRadians);
                vector.rotateAroundY(rotation.getY() * MathUtils.degreesToRadians);
                vector.rotateAroundZ(rotation.getZ() * MathUtils.degreesToRadians);
            }
            displayParticle(getParticleType().getValue(), location.add(vector), getParticleColor().getValue(), 1);
            location.subtract(vector);
        }
        if (noise) timer += noiseSpeed;
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }


    /**
     * sets starting radius of a sphere
     */
    public void setRadius(double value) {
        radius.setValue(value);
    }

    /**
     * sets multiplier of radius for every onTick() call
     */
    public void setRadiusMultiplier(double value) {
        radiusMultiply.setValue(value);
    }

    /**
     * sets incrementer of a radius for every onTick() call (increments after multiplying)
     */
    public void setRadiusIncrementer(double value) {
        radiusAdd.setValue(value);
    }

    /**
     * sets incrementer of a height for every onTick() call
     */
    public void setHeightIncrementer(double value) {
        YAdd.setValue(value);
    }


    /**
     * sets multiplier of noise amplitude. formula: (noise*multiplier)+incrementer
     */
    public void setNoiseAmplitudeMultiply(double value) {
        noiseAmplitudeMultiply = value;
    }

    /**
     * sets adder of noise amplitude. formula: (noise*multiplier)+adder
     */
    public void setNoiseAmplitudeAdder(double value) {
        noiseAmplitudeAdd = value;
    }

    /**
     * that setting affects work of noise. Less value - smoother noise effect will be.  Default: 0.5
     */
    public void setNoisePointMultiplier(double value) {
        noisePointMultiplier = value;
    }

    public void setNoiseSpeed(double value) {
        noiseSpeed = value;
    }


    /**
     * sets rotation of a circle
     * <p></p>
     * default: 0;0;0
     */
    public void setRotation(Vector value) {
        rotation.setValue(value);
    }

    public void setNoiseEnabled(boolean value) {
        noise = value;
    }
}
