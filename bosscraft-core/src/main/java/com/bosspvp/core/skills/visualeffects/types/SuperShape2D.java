package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectVariable;
import com.bosspvp.api.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SuperShape2D extends BaseEffect {
    private BaseEffectVariable<Double> n1 = new BaseEffectVariable<>(0.3, Double::parseDouble);
    private BaseEffectVariable<Double> n2 = new BaseEffectVariable<>(0.3, Double::parseDouble);
    private BaseEffectVariable<Double> n3 = new BaseEffectVariable<>(0.3, Double::parseDouble);
    private BaseEffectVariable<Double> amountOfAngles = new BaseEffectVariable<>(3.0, Double::parseDouble);
    private BaseEffectVariable<Double> scaleA = new BaseEffectVariable<>(1.0, Double::parseDouble);
    private BaseEffectVariable<Double> scaleB = new BaseEffectVariable<>(1.0, Double::parseDouble);
    private BaseEffectVariable<Double> PiMultiply = new BaseEffectVariable<>(2.2, Double::parseDouble);

    private BaseEffectVariable<Double> radius = new BaseEffectVariable<>(0.5, Double::parseDouble);
    private BaseEffectVariable<Double> radiusAdd = new BaseEffectVariable<>(0.0, Double::parseDouble);
    private BaseEffectVariable<Double> radiusMultiply = new BaseEffectVariable<>(1.0, Double::parseDouble);

    private BaseEffectVariable<Vector> rotation = new BaseEffectVariable<>(new Vector(),
            (it)->{
                String[] split = it.split(";");
                return new Vector(Double.parseDouble(split[0])*MathUtils.degreesToRadians,
                        Double.parseDouble(split[1])*MathUtils.degreesToRadians,
                        Double.parseDouble(split[2])*MathUtils.degreesToRadians);
            });


    private BaseEffectVariable<Boolean> drawAnimated = new BaseEffectVariable<>(false, Boolean::parseBoolean);
    private BaseEffectVariable<Integer> drawStepsPerTick = new BaseEffectVariable<>(1, Integer::parseInt);
    private BaseEffectVariable<Integer> brushesAmount = new BaseEffectVariable<>(1, Integer::parseInt);


    private int currentDrawStep;
    private int particles;
    private double step;
    private boolean initialized;

    /**
     * Displays a 2D Shape via superShape formula
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     */
    public SuperShape2D(VisualEffectsManager effectsManager) {

        super(effectsManager);

        getVariables().put("settings.n1", n1);
        getVariables().put("settings.n2", n2);
        getVariables().put("settings.n3", n3);
        getVariables().put("settings.amountOfAngles", amountOfAngles);
        getVariables().put("settings.scaleA", scaleA);
        getVariables().put("settings.scaleB", scaleB);
        getVariables().put("settings.PiMultiply", PiMultiply);
        getVariables().put("settings.radius", radius);
        getVariables().put("settings.radiusAdd", radiusAdd);
        getVariables().put("settings.radiusMultiply", radiusMultiply);
        getVariables().put("settings.rotation", rotation);
        getVariables().put("settings.drawAnimated", drawAnimated);
        getVariables().put("settings.drawStepsPerTick", drawStepsPerTick);
        getVariables().put("settings.brushesAmount", brushesAmount);
    }
    @Override
    public void onRun() {
        double radius = this.radius.getValue();
        double radiusAdd = this.radiusAdd.getValue();
        double radiusMultiply = this.radiusMultiply.getValue();
        double PiMultiply = this.PiMultiply.getValue();
        int particles = this.particles;
        boolean drawAnimated = this.drawAnimated.getValue();
        int drawStepsPerTick = this.drawStepsPerTick.getValue();
        int brushesAmount = this.brushesAmount.getValue();
        Vector rotation = this.rotation.getValue();

        if(!initialized){
            radius = (radius*radiusMultiply)+radiusAdd;
            particles = (int) (Math.PI * radius * PiMultiply / thickness.getValue());
            step = Math.PI*PiMultiply/particles;
            initialized = true;
        }
        Location location = getOrigin().getCurrentLocation();

        if(radiusMultiply!=1.0||radiusAdd!=0 || PiMultiply!=1.0) {
            radius = (radius * radiusMultiply) + radiusAdd;
            particles = (int) (Math.PI * radius * PiMultiply / thickness.getValue());

            step = Math.PI * PiMultiply / particles;
        }

        Vector vector=new Vector();
        if(drawAnimated) {
            int brushStep=particles/brushesAmount;
            for(int brush = 0; brush<brushesAmount;brush++){
                for (int i = 0; i < drawStepsPerTick&&particles>currentDrawStep; i++) {
                    double formula = superShape(step * (currentDrawStep+brushStep*brush)) * radius;
                    vector.setX(formula * MathUtils.fastSin(step * (currentDrawStep+brushStep*brush)));
                    vector.setZ(formula * MathUtils.fastCos(step * (currentDrawStep+brushStep*brush)));
                    vector.setY(1);
                    if (rotation != null) {
                        vector.rotateAroundX(rotation.getX() * MathUtils.degreesToRadians);
                        vector.rotateAroundY(rotation.getY() * MathUtils.degreesToRadians);
                        vector.rotateAroundZ(rotation.getZ() * MathUtils.degreesToRadians);
                    }
                    displayParticle(getParticleType().getValue(), location.add(vector));
                    location.subtract(vector);
                    currentDrawStep++;
                }
            }
            if(particles<=currentDrawStep) {
                finish();
            }
        }else {
            for (int i = 0; i < particles; i++) {
                double formula = superShape(step * i) * radius;
                vector.setX(formula * MathUtils.fastSin(step * i));
                vector.setZ(formula * MathUtils.fastCos(step * i));
                vector.setY(1);
                if (rotation != null) {
                    vector.rotateAroundX(rotation.getX() * MathUtils.degreesToRadians);
                    vector.rotateAroundY(rotation.getY() * MathUtils.degreesToRadians);
                    vector.rotateAroundZ(rotation.getZ() * MathUtils.degreesToRadians);
                }
                displayParticle(getParticleType().getValue(), location.add(vector));
                location.subtract(vector);
            }
        }
    }

    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }

    private double superShape(double theta){
        double scaleA = this.scaleA.getValue();
        double scaleB = this.scaleB.getValue();
        double n1 = this.n1.getValue();
        double n2 = this.n2.getValue();
        double n3 = this.n3.getValue();
        double amountOfAngles = this.amountOfAngles.getValue();

        double part1 = (1 / scaleA) * MathUtils.fastCos((theta * amountOfAngles) / 4);
        part1 = Math.abs(part1);
        part1 = Math.pow(part1, n2);

        double part2 = (1 / scaleB) * MathUtils.fastSin((theta * amountOfAngles) / 4);
        part2 = Math.abs(part2);
        part2 = Math.pow(part2, n3);

        double part3 = Math.pow(part1 + part2, 1 / n1);

        if (part3 == 0) {
            return 0;
        }

        return 1 / part3;
    }

}
