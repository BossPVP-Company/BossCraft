package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.ConfigArgument;
import com.bosspvp.api.skills.holder.ProvidedHolder;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Effect<T> extends Compilable<T> implements Listener {
    private HashMap<UUID,Integer> effectCounter = new HashMap<>();
    public Effect(@NotNull String id, ConfigArgument.@NotNull Arguments arguments) {
        super(id, arguments);
    }

    // The identifier factory.
    private val identifierFactory = IdentifierFactory(UUID.nameUUIDFromBytes(id.toByteArray()))

    /**
     * If the effect supports a certain [trigger].
     */
    fun supportsTrigger(trigger: Trigger) =
            Triggers.withParameters(parameters)(trigger)

    /**
     * Enable a permanent effect.
     *
     * @param player The player.
     * @param config The effect config.
     */
    public void enable(
            Player player,
            ProvidedHolder holder,
            config: ChainElement<T>,
            boolean isReload
    ) {
        if (isReload && !isShouldReload()) {
            return;
        }

        // Increment first to fix reload bug where effects are applied twice.
        effectCounter.put(player.getUniqueId(),effectCounter.get(player.getUniqueId())+1);
        int count = effectCounter.get(player.getUniqueId());

        val withHolder = config.config.applyHolder(holder, player);

        onEnable(player, withHolder, identifierFactory.makeIdentifiers(count), holder, config.compileData);
    }

    /**
     * Handle the enabling of this permanent effect.
     *
     * @param player The player.
     * @param config The config.
     * @param identifiers The identifiers.
     * @param compileData The compile data.
     */
    protected void onEnable(
            Player player,
            Config config,
            Identifiers identifiers,
            ProvidedHolder holder,
            T compileData
    ) {
        // Override when needed.
    }

    /**
     * Disable a permanent effect.
     *
     * @param player The player.
     */
    public void disable(
            Player player,
            ProvidedHolder holder,
            boolean isReload
    ) {
        if (isReload && !isShouldReload()) {
            return;
        }

        if (effectCounter.get(player.getUniqueId()) == 0) {
            return;
        }

        int count = effectCounter.get(player.getUniqueId()) - 1;
        effectCounter.put(player.getUniqueId(),count);
        onDisable(player, identifierFactory.makeIdentifiers(count), holder)
    }

    /**
     * Handle the disabling of this permanent effect.
     *
     * @param player The player.
     * @param identifiers The identifiers.
     */
    protected boolean onDisable(
            Player player,
            Identifiers identifiers,
            ProvidedHolder holder
    ) {
        // Override when needed.
    }

    /**
     * Trigger the effect.
     *
     * Returns if the execution was successful.
     *
     * @param trigger The trigger.
     * @param config The config.
     */
    public boolean trigger(
            DispatchedTrigger trigger,
            config: ChainElement<T>
    ){
        return onTrigger(config.config, trigger.data, config.compileData);
    }

    /**
     * Handle triggering.
     *
     * Returns if the execution was successful.
     *
     * @param data The trigger data.
     * @param compileData The compile data.
     */
    protected boolean onTrigger(
            Config config,
            TriggerData data,
            T compileData
    ) {
        return false
    }

    /**
     * If the effect should trigger.
     */
    boolean shouldTrigger(
            DispatchedTrigger trigger,
            config: ChainElement<T>
    ){
      return shouldTrigger(config.config, trigger.data, config.compileData);
    }

    /**
     * If the effect should trigger, ran before effect arguments in order
     * to prevent unnecessary calculations.
     */
    protected boolean shouldTrigger(
            Config config,
            TriggerData data,
            T compileData
    ) {
        return true;
    }


    @Override
    public void onRegister() {
        BossPlugin plugin = BossAPI.getInstance().getCorePlugin();
        BossAPI.getInstance().getCorePlugin().addTaskOnReload(() -> {
                    plugin.getEventManager().unregisterListener(this);
                    plugin.getEventManager().registerListener(this);
                    afterRegister();
                }
        );
    }


    public void afterRegister() {

    }
    public Set<TriggerParameter> getParameters(){
        return new HashSet<>();
    }
    public boolean isPermanent(){
        return getParameters().isEmpty();
    }

    public RunOrder getRunOrder(){
        return RunOrder.NORMAL;
    }
    public boolean isShouldReload(){
        return true;
    }
}
