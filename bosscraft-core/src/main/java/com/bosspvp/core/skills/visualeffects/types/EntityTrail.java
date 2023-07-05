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
        Location loc = entity.getLocation().subtract(entity.getVelocity());
        int particles = (int)(loc.distance(entity.getLocation())/thickness.getValue());

        if(particles==0){

            displayParticle(getParticleType().getValue(),entity.getLocation());

        }else {
            Vector step =  new Vector(entity.getVelocity().getX()/particles,entity.getVelocity().getY()/particles,entity.getVelocity().getZ()/particles);
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
