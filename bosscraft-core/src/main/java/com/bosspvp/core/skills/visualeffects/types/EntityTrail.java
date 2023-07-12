package com.bosspvp.core.skills.visualeffects.types;

import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class EntityTrail extends BaseEffect {
    private int ticksPassed;


    /**
     * Displays an Entity trail
     *
     * @param effectsManager <i>Sets manager where effect runnable will be saved<i/>
     */
    public EntityTrail(VisualEffectsManager effectsManager) {

        super(effectsManager);
    }

    @Override
    public void onRun() {
        Entity entity = getOrigin().getTargetEntity();
        if(entity==null){
            cancel(false);
            return;
        }
        Location entityLoc = entity.getLocation();
        Vector entityVelocity = entity.getVelocity();
        Location loc = entityLoc.subtract(entityVelocity);
        int particles = (int)(loc.distance(entityLoc)/thickness.getValue());

        if(particles==0){

            displayParticle(getParticleType().getValue(),entityLoc);

        }else {
            Vector step =  new Vector(entityVelocity.getX()/particles,entityVelocity.getY()/particles,entityVelocity.getZ()/particles);
            for(int i=0; i<particles; i++){
                loc.add(step);
            }
        }
        ticksPassed++;
    }


    @Override
    protected void onFinish() {

    }

    @Override
    protected void onClone(BaseEffect cloned) {

    }
}
