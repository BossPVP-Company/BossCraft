package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectVariable;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DynamicCircle extends BaseEffect {

    private BaseEffectVariable<Double> radius = new BaseEffectVariable<>(0.5,
            (it) -> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null, null, null, new ArrayList<>())
            )
    );

    private BaseEffectVariable<Double> radiusAdd = new BaseEffectVariable<>(0.0,
            (it) -> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null, null, null, new ArrayList<>())
            )
    );
    private BaseEffectVariable<Double> radiusMultiply = new BaseEffectVariable<>(1.0,
            (it) -> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null, null, null, new ArrayList<>())
            )
    );

    private BaseEffectVariable<Double> YAdd = new BaseEffectVariable<>(0.0,
            (it) -> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null, null, null, new ArrayList<>())
            )
    );
    private BaseEffectVariable<Vector> rotation = new BaseEffectVariable<>(new Vector(), (it) -> {
        String[] split = it.split(";");
        return new Vector(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    });
    private double currentY = 0;
    private double timer = 0;

    /**
     * Displays a Dynamic Circle
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     * @apiNote parameters examples:
     * <ul>
     *     <li>Explosion: <b>thickness:</b> 0.1; <b>radius:</b> 0.3; <b>radiusMultiplier:</b> 1.3; <b>iterations:</b> 10</li>
     *     <li>Cool effect: <b>thickness:</b> 0.1; <b>radius:</b> 0.3; <b>radiusMultiplier:</b> 1.15; <b>iterations:</b> 30; <b>noisePointMultiplier:</b> 0.2; <b>repeats:</b> -1; <b>repeatDelay:</b> -1</li>
     * </ul>
     */
    public DynamicCircle(@NotNull VisualEffectsManager effectsManager) {

        super(effectsManager);
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

        //for faster access in a loop
        double radius = this.radius.getValue();
        Vector rotation = this.rotation.getValue();
        boolean rotationEnabled = rotation.getX() != 0 || rotation.getY() != 0 || rotation.getZ() != 0;
        Particle particle = getParticleType().getValue();
        Color color = getParticleColor().getValue();

        int particles = (int) (Math.PI * radius * 2 / thickness.getValue());
        double step = Math.PI * 2 / particles;
        Vector vector = new Vector();
        for (int i = 0; i < particles; i++) {
            vector.setX(radius * MathUtils.fastSin(step * i));
            vector.setZ(radius * MathUtils.fastCos(step * i));
            //set to 0, to make rotation weird:D
            vector.setY(1);
            if (rotationEnabled) {
                MathUtils.rotateAroundX(vector,rotation.getX() * MathUtils.degreesToRadians);
                MathUtils.rotateAroundY(vector,rotation.getY() * MathUtils.degreesToRadians);
                MathUtils.rotateAroundZ(vector,rotation.getZ() * MathUtils.degreesToRadians);
            }
            displayParticle(particle, location.add(vector), color, 1);
            location.subtract(vector);
        }
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

}
