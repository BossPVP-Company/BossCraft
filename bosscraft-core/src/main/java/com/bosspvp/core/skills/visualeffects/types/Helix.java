package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectVariable;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Helix extends BaseEffect {

    private BaseEffectVariable<Double> radius = new BaseEffectVariable<>(0.5,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );


    private BaseEffectVariable<Integer> particleDrawPerTick = new BaseEffectVariable<>(4,
            (it)-> (int) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Double> radiusFunctionIncrementer = new BaseEffectVariable<>(0.5,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Vector> rotation = new BaseEffectVariable<>(new Vector(), (it)->{
        String[] split = it.split(";");
        return new Vector(Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2]));
    });

    private int currentDrawStep;
    private int particles;
    private double step;
    private double stepY;
    private boolean initialized=false;

    /**
     * Displays helix effect
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     *                       <p><p/>
     * @apiNote parameters examples:
     * <ul>
     *     <li>Standard: <b>thickness:</b> 0.06; <b>radius:</b> 0.8; <b>radiusFunctionIncrementer:</b> 0.5; <b>ParticleDrawPerTick:</b> 4</li>
     *     <li>2D Helix: <b>thickness:</b> 0.06; <b>radius:</b> 0.8; <b>radiusFunctionIncrementer:</b> 0.5; <b>ParticleDrawPerTick:</b> 4; <b>rotationX:</b> 0; <b>rotationZ:</b> 90</li>
     * </ul>
     */
    public Helix(@NotNull VisualEffectsManager effectsManager) {

        super(effectsManager);

        getVariables().put("settings.radius",radius);
        getVariables().put("settings.particleDrawPerTick",particleDrawPerTick);
        getVariables().put("settings.radiusFunctionIncrementer",radiusFunctionIncrementer);
        getVariables().put("settings.rotation",rotation);

    }

    @Override
    public void onRun() {
        if(particleDrawPerTick.getValue()*(currentDrawStep-1)>=particles){
            cancel(true);
            return;
        }
        Location startLocation = getOrigin().updateLocation();
        Location endLocation = getTarget().updateLocation();

        if(!initialized) {
            particles = (int) (Math.PI * radius.getValue() * 2 / thickness.getValue());
            step=Math.PI*2/particles;
            stepY=startLocation.distance(endLocation)/particles;
            initialized=true;
        }
        draw(startLocation);
        currentDrawStep++;
    }


    private void draw(Location start) {
        Vector v = new Vector();
        double radius;
        double radiusFunctionIncrementer=this.radiusFunctionIncrementer.getValue();
        double radiusOrigin=this.radius.getValue();
        Vector rotation=this.rotation.getValue();
        boolean rotationEnabled=rotation.getX()!=0||rotation.getY()!=0||rotation.getZ()!=0;
        int i_step=currentDrawStep*particleDrawPerTick.getValue();
        for (int i = 0; i < particleDrawPerTick.getValue(); i++) {
            radius=radiusOrigin* MathUtils.fastSin(Math.PI*i_step/particles+radiusFunctionIncrementer);
            v.setX(radius*MathUtils.fastSin(step*i_step));
            v.setZ(radius*MathUtils.fastCos(step*i_step));
            v.setY(i_step*stepY);
            if(rotationEnabled){
                v.rotateAroundX(rotation.getX() *  MathUtils.degreesToRadians);
                v.rotateAroundY(rotation.getY() *  MathUtils.degreesToRadians);
                v.rotateAroundZ(rotation.getZ() *  MathUtils.degreesToRadians);
            }
            displayParticle(getParticleType().getValue(), start.add(v));
            start.subtract(v);

            v.setX(radius*MathUtils.fastSin(step*i_step-Math.PI));
            v.setZ(radius*MathUtils.fastCos(step*i_step-Math.PI));
            v.setY(i_step*stepY);
            if(rotationEnabled){
                v.rotateAroundX(rotation.getX() *  MathUtils.degreesToRadians);
                v.rotateAroundY(rotation.getY() *  MathUtils.degreesToRadians);
                v.rotateAroundZ(rotation.getZ() *  MathUtils.degreesToRadians);
            }
            displayParticle(getParticleType().getValue(), start.add(v));
            start.subtract(v);
            i_step++;
        }

    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

}
