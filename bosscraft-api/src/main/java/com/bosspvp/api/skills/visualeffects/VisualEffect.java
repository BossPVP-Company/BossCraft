package com.bosspvp.api.skills.visualeffects;

import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public interface VisualEffect extends Cloneable, Registrable {

    //@TODO change this

    /**
     * set this to true if u want to use run() method yourself, separately from effectsManager.
     * <p>[!] You will not be able to use repeat feature, only manually as well.</p>
     *
     * @param manually whether to run this effect yourself
     */
    void runManually(boolean manually);

    //---------------

    /**
     * updates an effect
     */
    void run();

    /**
     * completes an effect
     */
    default void finish(){
        getEffectsRegistry().finishEffect(this);
    }

    /**
     * cancels an effect
     * @param callFinished whether to call complete() method
     */
    default void cancel(boolean callFinished){
        if(callFinished) finish();
    }

    /**
     * returns effect update delay in ticks
     */
    long getDelay();

    /**
     * returns effect update period in ticks
     */
    long getPeriod();

    /**
     * returns amount of iterations left
     * @apiNote
     * <p></p>
     * <i>'-1' -> infinite, can be stopped only manually.<i/>
     */
    int getIterations();

    /**
     * getEffectBuilder the amount of effect repeats.
     * <p></p>
     * The repeated effect is a different object but with
     * the same id and parameters
     * <p>-1 to makes effect repeat infinitely.</p>
     */
    int getRepeats();

    /**
     * getEffectBuilder the effect repeat delay specified by the amount of run() calls.
     * <p></p>
     * for example: delay 10 means after 10 iterations, the effect will be repeated.
     * <p></p>
     * If the effect wasn't finished yet, it will create a new effect with the same id
     * and continue its own iterations.
     *
     * <p>-1 to repeat an effect after it has been finished</p>
     */
    int getRepeatDelay();

    /**
     * getEffectBuilder the second location of an effect (for specific effect types)
     *
     * @return the effect location
     */
    @Nullable
    VisualEffectLocation getTarget();

    /**
     * returns an original location of effect
     *
     * @return the effect location
     */
    @NotNull
    VisualEffectLocation getOrigin();

    /**
     * is effect finished?
     */
    boolean isFinished();

    /**
     * Does effect updates asynchronously?
     */
    boolean isAsync();

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     */
    default void displayParticle(@NotNull Particle particle, @NotNull Location location) {
        displayParticle(particle, location, null, 1);
    }

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     * @param color the color of a particle
     * @param amount the amount of particles
     */
    default void displayParticle(@NotNull Particle particle,
                                 @NotNull Location location,
                                 @Nullable Color color,
                                 int amount) {
        ParticleUtils.display(particle, location, 0, 0, 0, 0, amount,
                color, null, 100);
    }

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     * @param color the color of the particle
     * @param offsetX the offset X of the particle (can be useful with >1 particles amount)
     * @param offsetY the offset Y of the particle (can be useful with >1 particles amount)
     * @param offsetZ the offset Z of the particle (can be useful with >1 particle amount)
     * @param amount the amount of particles
     */
    default void displayParticle(@NotNull Particle particle,
                                 @NotNull Location location,
                                 @Nullable Color color,
                                 float offsetX,
                                 float offsetY,
                                 float offsetZ,
                                 int amount) {

        ParticleUtils.display(particle, location, offsetX, offsetY, offsetZ, 0, amount,
                color, null, 100);
    }

    /**
     * display the particle
     *
     * @param particle the particle type
     * @param location the location where to display
     * @param color the color of the particle
     * @param offsetX the offset X of the particle (can be useful with >1 particles amount)
     * @param offsetY the offset Y of the particle (can be useful with >1 particles amount)
     * @param offsetZ the offset Z of the particle (can be useful with >1 particle amount)
     * @param amount the amount of particles
     * @param speed the speed of the particle
     * @param material the material of the particle
     * @param range the display range of the particle
     */
    default void displayParticle(@NotNull Particle particle,
                                 @NotNull Location location,
                                 @Nullable Color color,
                                 float offsetX,
                                 float offsetY,
                                 float offsetZ,
                                 int amount,
                                 float speed,
                                 @Nullable Material material,
                                 double range) {

        ParticleUtils.display(particle, location, offsetX, offsetY, offsetZ, speed, amount,
                color, material, range);
    }

    @NotNull HashMap<String, VisualEffectVariable<?>> getVariables();
    <T> void setVariable(String key, T value);


    /**
     * getEffectBuilder effects manager the effect attached to
     *
     * @return The effects manager
     */
    @NotNull
    VisualEffectsManager getEffectsRegistry();

    /**
     * getEffectBuilder effect id
     *
     * @return The effect id
     */
    @NotNull String getId();



    VisualEffect clone() throws CloneNotSupportedException;
}
