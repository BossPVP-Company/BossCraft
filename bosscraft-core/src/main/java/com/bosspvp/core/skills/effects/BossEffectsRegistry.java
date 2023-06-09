package com.bosspvp.core.skills.effects;


import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.effects.*;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.filters.FilterList;
import com.bosspvp.core.skills.effects.types.*;
import com.bosspvp.core.skills.effects.types.attribute.*;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import com.bosspvp.core.skills.effects.types.chance.EffectDontConsumeLapisChance;
import com.bosspvp.core.skills.effects.types.chance.EffectDontConsumeXpChance;
import com.bosspvp.core.skills.effects.types.multiplier.EffectHungerMultiplier;
import com.bosspvp.core.skills.effects.types.multiplier.EffectPotionDurationMultiplier;
import com.bosspvp.core.skills.effects.types.multiplier.EffectReelSpeedMultiplier;
import com.bosspvp.core.skills.effects.types.multiplier.EffectXpMultiplier;
import com.bosspvp.core.skills.effects.types.particles.EffectVisualEffect;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class BossEffectsRegistry extends Registry<Effect<?>> implements EffectsRegistry {
    @Getter
    private final BossPlugin plugin;
    private HashMap<String, Chain> identifiedChains = new HashMap<>();

    @Override
    public @Nullable Chain getChainByID(@NotNull String id) {
        return identifiedChains.get(id);
    }

    /**
     * Register a new [chain] with a certain [id].
     */
    @Override
    public void register(@NotNull String id, @NotNull Chain chain) {
        identifiedChains.put(id, chain);
    }

    /**
     * Compile a list of [configs] into an EffectList in a given [context].
     */
    @Override
    public EffectList compile(@NotNull Collection<Config> configs, @NotNull ViolationContext context) {
        List<EffectBlock> list = new ArrayList<>();
        for (Config entry : configs) {
            EffectBlock block = compile(entry, context);
            if (block == null) continue;
            list.add(block);
        }
        return new EffectList(list);
    }

    @Override
    public @Nullable EffectBlock compile(@NotNull Config cfg, @NotNull ViolationContext context) {

        SkillsManager skillsManager = plugin.getSkillsManager();

        //@TODO cfg.separatorAmbivalent();
        // I'm not sure yet whether it is really needed. I'll leave it for now.
        Config config = cfg;

        Config args = config.getSubsectionOrNull("args");
        if(args==null){
            plugin.getLogger().warning("Effect block is missing args section.");
            return null;
        }

        var arguments = skillsManager.getEffectArgumentsRegistry().compile(args, context.with("args"));
        var conditions = skillsManager.getConditionsRegistry().compile(config.getSubsectionList("conditions"), context.with("conditions"));

        var mutators  = skillsManager.getMutatorRegistry()
                .compile(config.getSubsectionList("mutators"), context.with("mutators"));
        var filterSection = config.getSubsection("filters");
        FilterList filters;
        if(filterSection==null){
            filters = new FilterList(new ArrayList<>());
        }else{
            filters = skillsManager.getFilterRegistry()
                    .compile(filterSection, context.with("filters"));
        }
        List<Trigger> triggers = new ArrayList<>();
        for (String entry : config.getStringList("triggers")) {
            Trigger trigger = skillsManager.getTriggersRegistry().get(entry);
            if (trigger == null) continue;
            triggers.add(trigger);
        }

        var effectConfigs = config.hasPath("id") ? List.of(config) : config.getSubsectionList("effects");


        boolean directIDSpecified = config.hasPath("id");

        ChainExecutor executor = skillsManager.getChainExecutorsRegistry()
                .getOrNormal(args.getStringOrNull("run-type"));

        var chain = compileChain(effectConfigs, executor, context, directIDSpecified);
        if(chain==null) {
            return null;
        }

        var permanentEffects = chain.getList().stream().filter (it -> it.getEffect().isPermanent()).toList();
        var triggeredEffects = chain.getList().stream().filter (it -> !it.getEffect().isPermanent()).toList();

        if (!triggers.isEmpty() && !permanentEffects.isEmpty()) {
            context.log(
                    //@TODO add "effects: ${permanentEffects.joinToString(", ") { it.effect.id }}!"
                    new ConfigViolation(
                            "triggers", "Triggers are not allowed on permanent "
                    )
            );
            return null;
        }

        boolean hasNoPermanent = false;
        for(var block : chain.getList()) {
            if(!block.getEffect().isPermanent()) {
                hasNoPermanent = true;
                break;
            }
        }
        if (triggers.isEmpty() && hasNoPermanent){
            context.log(
                    //@TODO add "triggered effects: ${triggeredEffects.joinToString(", ") { it.effect.id }}!"
                    new ConfigViolation(
                            "triggers", "You must specify at least one trigger for "
                    )
            );
            return null;
        }

        var isInvalid = false;
        for (var element : chain.getList()) {
            for (var trigger : triggers) {
                if (!element.getEffect().supportsTrigger(trigger)) {
                    isInvalid = true;
                    context.log(
                            new ConfigViolation(
                                    "triggers",
                                    element.getEffect().getId()+" does not support trigger "+trigger.getId()
                            )
                    );
                    break;
                }
            }
        }

        if (isInvalid) {
            return null;
        }

        return new EffectBlock(
                plugin,
                UUID.randomUUID(),
                args,
                chain,
                triggers,
                arguments,
                conditions,
                mutators,
                filters,
                directIDSpecified
        );
    }

    @Override
    public @Nullable Chain compileChain(@NotNull Collection<Config> configs,
                                        @NotNull ChainExecutor executor,
                                        @NotNull ViolationContext context
    ) {
        return compileChain(configs, executor, context, false);
    }

    @Nullable
    private Chain compileChain(Collection<Config> configs,
                               ChainExecutor executor,
                               ViolationContext context,
                               boolean directIDSpecified// If it's configured with 'id', rather than 'effects'
    ) {
        List<ChainElement<?>> elements = new ArrayList<>();
        for(var config : configs) {
            var element = compileElement(config, context);
            if(element==null) continue;
            elements.add(element);
        }
        if((elements.size() > 1 || !directIDSpecified) && elements.stream().anyMatch(it -> it.getEffect().isPermanent())) {
            context.log(
                    new ConfigViolation(
                            "effects",
                            "Permanent effects ("+
                                    elements.stream()
                                            .filter(it -> it.getEffect().isPermanent())
                                            .map(it -> it.getEffect().getId())
                                            .collect(Collectors.joining(", "))
                                    +") are not allowed in chains!"
                    )
            );
            return null;
        }
        return new Chain(elements, executor);
    }

    private ChainElement<?> compileElement(Config config, ViolationContext context){
        var id = config.getString("id");
        var effect = this.get(id);
        if(effect==null) {
            context.log(
                    new ConfigViolation(
                            "id",
                            "Invalid effect ID specified: "+id+"!"
                    )
            );
            return null;
        }
        return makeElement(effect, config, context);
    }

    private<T> ChainElement<T> makeElement(Effect<T> effect, Config config, ViolationContext context) {
        var args = config.getSubsection("args");

        if (!effect.checkConfig(args, context.with("args"))) {
            return null;
        }

        try {
            var compileData = effect.makeCompileData(args, context.with("args"));

            var arguments = plugin.getSkillsManager().getEffectArgumentsRegistry().compile(args, context.with("args"));
            var conditions = plugin.getSkillsManager().getConditionsRegistry().compile(config.getSubsectionList("conditions"), context.with("conditions"));

            var mutators = plugin.getSkillsManager()
                    .getMutatorRegistry().compile(config.getSubsectionList("mutators"), context.with("mutators"));
            var filterSection = config.getSubsection("filters");
            FilterList filters;
            if(filterSection==null){
                filters = new FilterList(new ArrayList<>());
            }else{
                filters = plugin.getSkillsManager().getFilterRegistry()
                        .compile(filterSection, context.with("filters"));
            }
            return new ChainElement<>(
                    plugin,
                    effect,
                    args,
                    compileData,
                    arguments,
                    conditions,
                    mutators,
                    filters
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Registry<Effect<?>> getRegistry() {
        return this;
    }

    public BossEffectsRegistry(BossPlugin plugin) {
        this.plugin = plugin;
        register(new EffectIgnite(plugin));
        register(new EffectArmor(plugin));
        register(new EffectBonusHealth(plugin));
        register(new EffectArmorToughness(plugin));
        register(new EffectAttackSpeedMultiplier(plugin));
        register(new EffectMovementSpeedMultiplier(plugin));
        register(new EffectDamageMultiplier(plugin));
        register(new EffectXpMultiplier(plugin));
        register(new EffectDamageTwice(plugin));
        register(new EffectVisualEffect(plugin));
        register(new EffectSendMessage(plugin));
        register(new EffectCancelEvent(plugin));
        register(new EffectAddHolder(plugin));
        register(new EffectMultiplyDrops(plugin));
        register(new EffectPotionEffect(plugin));
        register(new EffectDropRandomItem(plugin));
        register(new EffectRegenMultiplier(plugin));
        register(new EffectHungerMultiplier(plugin));
        register(new EffectBleed(plugin));
        register(new EffectRepairItem(plugin));
        register(new EffectRemovePotionEffect(plugin));
        register(new EffectPotionDurationMultiplier(plugin));
        register(new EffectClearInvulnerability(plugin));
        register(new EffectReelSpeedMultiplier(plugin));
        register(new EffectDontConsumeXpChance(plugin));
        register(new EffectDontConsumeLapisChance(plugin));
        register(new EffectBrewTimeMultiplier(plugin));
        register(new EffectRunCommand(plugin));
        register(new EffectRunPlayerCommand(plugin));
    }


}
