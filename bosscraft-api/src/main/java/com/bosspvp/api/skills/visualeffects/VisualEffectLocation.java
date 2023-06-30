package com.bosspvp.api.skills.visualeffects;

import com.bosspvp.api.tuples.Pair;
import com.bosspvp.api.tuples.PairRecord;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


public interface VisualEffectLocation {

    /**
     * get original location (i.e starting loc)
     *
     * @return The location
     */
    @NotNull
    Location getOrigin();

    /**
     * get targeted entity
     *
     * @return The entity
     */
    @Nullable
    Entity getTargetEntity();

    /**
     * get current effect location
     *
     * @return The location
     */
    @NotNull
    Location getCurrentLocation();

    /**
     * updates the current effect location
     *
     * @return The location
     */
    @NotNull
    Location updateLocation();


    /**
     * set offset x,y,z
     *
     * @param offsetX the X value
     * @param offsetY the Y value
     * @param offsetZ the Z value
     * @return this
     */
    @NotNull
    VisualEffectLocation setOffsetXYZ(double offsetX, double offsetY, double offsetZ);

    /**
     * set offset yaw
     *
     * @param offsetYaw the value
     * @return this
     */
    @NotNull
    VisualEffectLocation setOffsetYaw(float offsetYaw);

    /**
     * set offset pitch
     *
     * @param offsetPitch the value
     * @return this
     */
    @NotNull
    VisualEffectLocation setOffsetPitch(float offsetPitch);


    /**
     * set velocity x,y,z
     *
     * @param velocityX the X value
     * @param velocityY the Y value
     * @param velocityZ the Z value
     * @return this
     */
    @NotNull
    VisualEffectLocation setVelocityXYZ(double velocityX,double velocityY,double velocityZ);

    /**
     * set velocity yaw
     *
     * @param velocityYaw the value
     * @return this
     */
    @NotNull
    VisualEffectLocation setVelocityYaw(float velocityYaw);

    /**
     * set velocity pitch
     *
     * @param velocityPitch the value
     * @return this
     */
    @NotNull
    VisualEffectLocation setVelocityPitch(float velocityPitch);


    /**
     * set the Value limit to x y z.
     * Null to disable
     *
     * @param limitX the value limit of X
     * @param limitY the value limit of Y
     * @param limitZ the value limit of Z
     * @return this
     */
    @NotNull
    VisualEffectLocation setLimitXYZ(@Nullable ValueLimit<Double> limitX,
                               @Nullable ValueLimit<Double> limitY,
                               @Nullable ValueLimit<Double> limitZ);

    /**
     * set the Value limit of yaw
     * Null to disable
     *
     * @param limitYaw the value limit of Yaw
     * @return this
     */
    @NotNull
    VisualEffectLocation setLimitYaw(@Nullable ValueLimit<Float> limitYaw);

    /**
     * set the Value limit of pitch
     * Null to disable
     *
     * @param limitPitch the value limit of Pitch
     * @return this
     */
    @NotNull
    VisualEffectLocation setLimitPitch(@Nullable ValueLimit<Float> limitPitch);


    Set<PairRecord<String,VisualEffectVariable<?>>> getVariables();


    VisualEffectLocation clone() throws CloneNotSupportedException;
}
