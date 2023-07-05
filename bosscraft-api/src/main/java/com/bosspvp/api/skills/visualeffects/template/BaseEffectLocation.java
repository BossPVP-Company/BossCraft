package com.bosspvp.api.skills.visualeffects.template;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.context.PlaceholderContext;
import com.bosspvp.api.skills.visualeffects.ValueLimit;
import com.bosspvp.api.skills.visualeffects.VisualEffectLocation;
import com.bosspvp.api.skills.visualeffects.VisualEffectVariable;
import com.bosspvp.api.tuples.PairRecord;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Set;

public class BaseEffectLocation implements VisualEffectLocation {
    @Getter @NotNull private final Location origin;
    @Getter @NotNull private Location currentLocation;
    @Getter @Nullable private Entity targetEntity;

    private BaseEffectVariable<Double> offsetX = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Double> offsetY = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Double> offsetZ = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Float> offsetYaw = new BaseEffectVariable<>(0.0f,
            (it)-> (float) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Float> offsetPitch = new BaseEffectVariable<>(0.0f,
            (it)-> (float)BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private boolean offsetApplied;

    private BaseEffectVariable<Double> velocityX = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Double> velocityY = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Double> velocityZ = new BaseEffectVariable<>(0.0,
            (it)-> BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Float> velocityYaw = new BaseEffectVariable<>(0.0f,
            (it)->(float) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private BaseEffectVariable<Float> velocityPitch = new BaseEffectVariable<>(0.0f,
            (it)->(float) BossAPI.getInstance().evaluate(it,
                    new PlaceholderContext(null,null,null,new ArrayList<>())
            )
    );
    private int velocityStep;

    private ValueLimit<Double> limitX;
    private ValueLimit<Double> limitY;
    private ValueLimit<Double> limitZ;
    private ValueLimit<Float> limitYaw;
    private ValueLimit<Float> limitPitch;

    public BaseEffectLocation(@NotNull Location location){
        this.currentLocation=location;

        origin = currentLocation.clone();
    }
    public BaseEffectLocation(@NotNull Entity targetEntity){
        this.targetEntity=targetEntity;
        currentLocation=targetEntity.getLocation();

        origin = currentLocation.clone();
    }

    @Override
    public @NotNull Location updateLocation() {
        if(targetEntity!=null){
            currentLocation=targetEntity.getLocation();
            currentLocation.add(offsetX.getValue()+(velocityX.getValue()*velocityStep),
                    offsetY.getValue() + (velocityY.getValue()*velocityStep),
                    offsetZ.getValue() + (velocityZ.getValue()*velocityStep));

            currentLocation.setYaw(currentLocation.getYaw() +
                    offsetYaw.getValue() + (velocityYaw.getValue()*velocityStep));
            currentLocation.setPitch(currentLocation.getPitch() +
                    offsetPitch.getValue() + (velocityPitch.getValue()*velocityStep));

        }else{
            if(!offsetApplied) {
                currentLocation.add(offsetX.getValue(), offsetY.getValue(), offsetZ.getValue());
                currentLocation.setYaw(currentLocation.getYaw() + offsetYaw.getValue());
                currentLocation.setPitch(currentLocation.getPitch() + offsetPitch.getValue());
                offsetApplied=true;
            }else{
                currentLocation.add(velocityX.getValue(), velocityY.getValue(), velocityZ.getValue());
                currentLocation.setYaw(currentLocation.getYaw() + velocityYaw.getValue());
                currentLocation.setPitch(currentLocation.getPitch() + velocityPitch.getValue());
            }

        }
        applyLimits();

        velocityStep++;
        return currentLocation;
    }
    private void applyLimits(){
        if(limitX!=null){
            currentLocation.setX(limitX.limit(currentLocation.getX()));
        }
        if(limitY!=null){
            currentLocation.setY(limitY.limit(currentLocation.getY()));
        }
        if(limitZ!=null){
            currentLocation.setZ(limitZ.limit(currentLocation.getZ()));
        }
        if(limitYaw!=null){
            currentLocation.setYaw(limitYaw.limit(currentLocation.getYaw()));
        }
        if(limitPitch!=null){
            currentLocation.setPitch(limitPitch.limit(currentLocation.getPitch()));
        }
    }

    @Override
    public @NotNull VisualEffectLocation setOffsetXYZ(double offsetX, double offsetY, double offsetZ) {
        this.offsetX.setValue(offsetX);
        this.offsetY.setValue(offsetY);
        this.offsetZ.setValue(offsetZ);
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setOffsetYaw(float offsetYaw) {
        this.offsetYaw.setValue(offsetYaw);
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setOffsetPitch(float offsetPitch) {
        this.offsetPitch.setValue(offsetPitch);
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setVelocityXYZ(double velocityX, double velocityY, double velocityZ) {
        this.velocityX.setValue(velocityX);
        this.velocityY.setValue(velocityY);
        this.velocityZ.setValue(velocityZ);
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setVelocityYaw(float velocityYaw) {
        this.velocityYaw.setValue(velocityYaw);
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setVelocityPitch(float velocityPitch) {
        this.velocityPitch.setValue(velocityPitch);
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setLimitXYZ(@Nullable ValueLimit<Double> limitX, @Nullable ValueLimit<Double> limitY, @Nullable ValueLimit<Double> limitZ) {
        this.limitX=limitX;
        this.limitY=limitY;
        this.limitZ=limitZ;
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setLimitYaw(@Nullable ValueLimit<Float> limitYaw) {
        this.limitYaw=limitYaw;
        return this;
    }

    @Override
    public @NotNull VisualEffectLocation setLimitPitch(@Nullable ValueLimit<Float> limitPitch) {
        this.limitPitch=limitPitch;
        return this;
    }

    @Override
    public Set<PairRecord<String, VisualEffectVariable<?>>> getVariables() {
        return Set.of(
                new PairRecord<>("offset.x",offsetX),
                new PairRecord<>("offset.y",offsetY),
                new PairRecord<>("offset.z",offsetZ),
                new PairRecord<>("offset.yaw",offsetYaw),
                new PairRecord<>("offset.pitch",offsetPitch),
                new PairRecord<>("velocity.x",velocityX),
                new PairRecord<>("velocity.y",velocityY),
                new PairRecord<>("velocity.z",velocityZ),
                new PairRecord<>("velocity.yaw",velocityYaw),
                new PairRecord<>("velocity.pitch",velocityPitch)
        );
    }

    @Override
    public VisualEffectLocation clone() throws CloneNotSupportedException {
        try {
            BaseEffectLocation loc=(BaseEffectLocation) (super.clone());

            if(loc.targetEntity==null) loc.currentLocation = loc.origin.clone();

            if(loc.limitX!=null){
                loc.limitX = loc.limitX.clone();
            }
            if(loc.limitY!=null){
                loc.limitY = loc.limitY.clone();
            }
            if(loc.limitZ!=null){
                loc.limitZ = loc.limitZ.clone();
            }
            if(loc.limitYaw!=null){
                loc.limitYaw = loc.limitYaw.clone();
            }
            if(loc.limitPitch!=null){
                loc.limitPitch = loc.limitPitch.clone();
            }
            return loc;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
