package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.*;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectVariable;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class SnowFlake extends BaseEffect {
    private Random random;
    private final VisualEffectVariable<Vector> snowFlakeRotation = new BaseEffectVariable<>(new Vector(), (it)->{
        String[] split = it.split(";");
        return new Vector(Double.parseDouble(split[0])*MathUtils.degreesToRadians,
                Double.parseDouble(split[1])*MathUtils.degreesToRadians,
                Double.parseDouble(split[2])*MathUtils.degreesToRadians);
    });

    private final VisualEffectVariable<Vector> partRotation = new BaseEffectVariable<>(new Vector(0,Math.PI / 3,0), (it)->{
        String[] split = it.split(";");
        return new Vector(Double.parseDouble(split[0])*MathUtils.degreesToRadians,
                Double.parseDouble(split[1])*MathUtils.degreesToRadians,
                Double.parseDouble(split[2])*MathUtils.degreesToRadians);
    });
    private final VisualEffectVariable<Integer> amountOfParts = new BaseEffectVariable<>(2,
            (it)-> (int) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private final VisualEffectVariable<Double> angleUpdateLimit = new BaseEffectVariable<>(Math.PI/6,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private final VisualEffectVariable<Double> randomAmplitudeBounds=new BaseEffectVariable<>(0.4,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );

    private final VisualEffectVariable<Double> size=new BaseEffectVariable<>(20.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );

    private final VisualEffectVariable<Boolean> displayDrawnParticles=new BaseEffectVariable<>(false,
            Boolean::parseBoolean
    );
    private boolean drawFinished=false;
    private boolean initialized;

    private ArrayList<CustomParticle> snowflake;

    private CustomParticle current;
    /**
     * Draws SnowFlake with random shape
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     *                       <p><p/>
     * @apiNote parameters examples:
     * <ul>
     *     <li>Standard: <b>thickness:</b> 0.6; <b>size:</b> 10; <b>snowFlakeRotation:</b> 90;0;0</li>
     *     <li>SnowFLake with 3 parts: <b>partRotation:</b> 120</li>
     *     <li>SnowFLake with 4 parts: <b>partRotation:</b> 90</li>
     * </ul>
     */
    public SnowFlake(VisualEffectsManager effectsManager) {
        super(effectsManager);

        getVariables().put("settings.snowFlakeRotation",snowFlakeRotation);
        getVariables().put("settings.partRotation",partRotation);
        getVariables().put("settings.amountOfParts",amountOfParts);
        getVariables().put("settings.angleUpdateLimit",angleUpdateLimit);
        getVariables().put("settings.randomAmplitudeBounds",randomAmplitudeBounds);
        getVariables().put("settings.size",size);
        getVariables().put("settings.displayDrawnParticles",displayDrawnParticles);
    }
    @Override
    public void onRun() {
        Location location = getOrigin().updateLocation();

        if(!initialized){
            snowflake= new ArrayList<>();
            random=new Random(System.nanoTime());
            initialized=true;
        }
        draw(location);
    }

    void draw(Location loc) {
        if(!drawFinished) {
            current = new CustomParticle(size.getValue() / 2, 0);
            current.update();
            boolean b = false;
            //updates pos value of current particle until pos.x reaches 0.1 or less, or if  collides with other particles
            while (!current.finished() && !current.collides()) {
                current.update();
                b = true;
            }
            if (b) {
                snowflake.add(current);
            } else {
                drawFinished = true;
            }
        }
        Vector rotation=new Vector();
        Vector partRotation = this.partRotation.getValue();
        boolean displayDrawnParticles = this.displayDrawnParticles.getValue();
        for(int i=0;i<amountOfParts.getValue();i++) {
            rotation.add(partRotation);
            if(!drawFinished){
                current.show(loc, rotation,false);
                current.show(loc,rotation,true);
            }
            if(displayDrawnParticles) {
                for (CustomParticle p : snowflake) {
                    p.show(loc, rotation, false);
                    p.show(loc, rotation, true);
                }
            }
        }
        if(drawFinished&&!displayDrawnParticles){
            cancel(true);
        }
    }

    private class CustomParticle {

        Vector pos;

        CustomParticle(double size, double angle) {
            pos = MathUtils.vectorFromAngle(angle);
            pos.multiply(size);
        }

        void update() {
            pos.setX(pos.getX()-0.1);
            pos.setY(pos.getY()+random.nextDouble()*randomAmplitudeBounds.getValue()-randomAmplitudeBounds.getValue()/2);
            double angle = MathUtils.angleFromVector(pos);
            angle = Math.min(angle<0?0:angle,angleUpdateLimit.getValue());
            double magnitude = MathUtils.getMagnitude(pos);
            pos = MathUtils.vectorFromAngle(angle);
            MathUtils.setMagnitude(pos,magnitude);
        }

        void show(Location loc, Vector rotation, boolean reflect) {
            Vector vec= pos.clone();
            if(reflect){
                vec.setY(-vec.getY());
            }
            vec.rotateAroundX(snowFlakeRotation.getValue().getX() + rotation.getX());
            vec.rotateAroundY(snowFlakeRotation.getValue().getY() + rotation.getY());
            vec.rotateAroundZ(snowFlakeRotation.getValue().getZ() + rotation.getZ());
            displayParticle(getParticleType().getValue(),loc.add(vec));
            loc.subtract(vec);
        }

        boolean collides() {
            boolean result = false;
            for (CustomParticle s : snowflake) {
                double d = s.pos.distance(pos);
                if (d < thickness.getValue()) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        boolean finished() {
            return (pos.getX() < 0.1);
        }
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

}
