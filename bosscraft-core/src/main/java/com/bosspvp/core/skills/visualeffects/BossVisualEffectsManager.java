package com.bosspvp.core.skills.visualeffects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectBuilder;
import com.bosspvp.api.skills.visualeffects.VisualEffectsManager;
import com.bosspvp.api.skills.visualeffects.template.BaseEffectBuilder;
import com.bosspvp.core.skills.visualeffects.types.DynamicCircle;
import com.bosspvp.core.skills.visualeffects.types.Helix;
import com.bosspvp.core.skills.visualeffects.types.SingleParticle;
import com.bosspvp.core.skills.visualeffects.types.SnowFlake;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class BossVisualEffectsManager implements VisualEffectsManager {
    private BossPlugin plugin;
    private HashMap<String,VisualEffectBuilder> effectBuilders = new HashMap<>();
    private HashMap<VisualEffect, BukkitTask> runningEffects = new HashMap<>();

    public BossVisualEffectsManager(BossPlugin plugin) {
        this.plugin = plugin;
        registerEffectBuilder(new BaseEffectBuilder("dynamic_circle",
                ()->new DynamicCircle(this))
        );
        registerEffectBuilder(new BaseEffectBuilder("helix",
                ()->new Helix(this))
        );
        registerEffectBuilder(new BaseEffectBuilder("single_particle",
                ()->new SingleParticle(this))
        );
        registerEffectBuilder(new BaseEffectBuilder("snow_flake",
                ()->new SnowFlake(this))
        );
    }
    @Override
    public @Nullable VisualEffectBuilder getEffectBuilder(@NotNull String id) {
        //@TODO
        return null;
    }

    @Override
    public void registerEffectBuilder(@NotNull VisualEffectBuilder effectBuilder) {
        effectBuilders.put(effectBuilder.getId(), effectBuilder);
    }

    @Override
    public String startEffect(@NotNull VisualEffect effect) {
        if(runningEffects.containsKey(effect)) {
            getPlugin().getLogger().info("EffectsManager: Tried to start an effect which is already active");
            return null;
        }
        var s = getPlugin().getScheduler();
        if(effect.getPeriod() < 1) {
            getPlugin().getLogger().info("EffectsManager: Period can not be less than 1!");
            return null;
        }
        if(effect.getDelay() < 0) {
            getPlugin().getLogger().info("EffectsManager: Delay can not be less than 0!");
            return null;
        }
        if(effect.getIterations() == 0) {
            getPlugin().getLogger().info("EffectsManager: Amount of iterations cannot be 0");
            return null;
        }
        if(effect.getIterations() < -1) {
            getPlugin().getLogger().info("EffectsManager: Amount of iterations cannot be less than -1");
            return null;
        }
        BukkitTask task;
        if(effect.getIterations() == 1 && effect.getDelay() > 0){
            if(effect.isAsync()) {
                task = s.runLaterAsync(effect.getDelay(),effect::run);
            } else {
                task = s.runLater(effect.getDelay(),effect::run);
            }
        } else {
            if(effect.isAsync()) {
                task = s.runTimerAsync(effect.getDelay(), effect.getPeriod(), effect::run);
            } else {
                task = s.runTimer(effect.getDelay(), effect.getPeriod(), effect::run);
            }
        }
        runningEffects.put(effect, task);
        return effect.getId();
    }

    @Override
    public void finishEffect(@NotNull VisualEffect effect) {
        var task = runningEffects.get(effect);
        if(task != null) {
            task.cancel();
            runningEffects.remove(effect);
        }
    }

    @Override
    public void cancelEffectTask(@NotNull String id) {
        for(var effect : runningEffects.keySet()) {
            if(effect.getId().equals(id)) {
                effect.cancel(false);
            }
        }
    }

    @Override
    public void cancelAllEffectTasks() {
            for(var effect : runningEffects.keySet()) {
                effect.cancel(false);
            }
    }

    @Override
    public @NotNull List<VisualEffect> getEffectsByParentTaskID(@NotNull String id) {
        /*runningEffects.keys.filter { it.id == id }.toMutableList()*/
        return runningEffects.keySet().stream().filter(effect -> effect.getId().equals(id)).toList();
    }

    @Override
    public Registry<VisualEffect> getRegistry() {
        //@TODO
        return null;
    }

    @Override
    public @NotNull BossPlugin getPlugin() {
        return plugin;
    }
}
