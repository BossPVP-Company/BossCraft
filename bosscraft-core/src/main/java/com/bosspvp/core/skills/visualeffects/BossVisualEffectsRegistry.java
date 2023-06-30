package com.bosspvp.core.skills.visualeffects;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.visualeffects.VisualEffect;
import com.bosspvp.api.skills.visualeffects.VisualEffectsRegistry;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class BossVisualEffectsRegistry implements VisualEffectsRegistry {
    private BossPlugin plugin;
    private HashMap<VisualEffect, BukkitTask> runningEffects = new HashMap<>();

    public BossVisualEffectsRegistry(BossPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public @Nullable VisualEffect get(@NotNull String id) {
        //@TODO
        return null;
    }

    @Override
    public void register(@NotNull VisualEffect triggerGroup) {
        //@TODO
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
    public void cancelEffectsByTaskID(@NotNull String id) {
        for(var effect : runningEffects.keySet()) {
            if(effect.getId().equals(id)) {
                effect.cancel(false);
            }
        }
    }

    @Override
    public void cancelAllEffects() {
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
