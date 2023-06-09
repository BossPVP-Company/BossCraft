package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.SkillsManager;
import com.bosspvp.api.skills.effects.executors.ChainExecutor;
import com.bosspvp.api.skills.effects.types.EffectIgnite;
import com.bosspvp.api.skills.triggers.Trigger;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class EffectsRegistry extends Registry<Effect<?>> {
    private final BossPlugin plugin;
    private HashMap<String, Chain> identifiedChains = new HashMap<>();

    @Nullable
    public Chain getChainByID(String id) {
        return identifiedChains.get(id);
    }

    /**
     * Register a new [chain] with a certain [id].
     */
    public void register(String id, Chain chain) {
        identifiedChains.put(id, chain);
    }

    /**
     * Compile a list of [configs] into an EffectList in a given [context].
     */
    public EffectList compile(Collection<Config> configs, ViolationContext context) {
        List<EffectBlock> list = new ArrayList<>();
        for (Config entry : configs) {
            EffectBlock block = compile(entry, context);
            if (block == null) continue;
            list.add(block);
        }
        return new EffectList(list);
    }

    @Nullable
    public EffectBlock compile(Config cfg, ViolationContext context) {
        SkillsManager skillsManager = plugin.getSkillsManager();

        //@TODO cfg.separatorAmbivalent();
        // I'm not sure yet whether it is really needed. I'll leave it for now.
        Config config = cfg;

        Config args = config.getSubsection("args");

        var arguments = skillsManager.getEffectArgumentsRegistry().compile(args, context.with("args"));
        var conditions = skillsManager.getConditionsRegistry().compile(config.getSubsectionList("conditions"), context.with("conditions"));

        //@TODO
        /* val mutators = Mutators.compile(config.getSubsections("mutators"), context.with("mutators"))
        val filters = Filters.compile(config.getSubsection("filters"), context.with("filters"))
       */
        List<Trigger> triggers = new ArrayList<>();
        for (String entry : config.getStringList("triggers")) {
            Trigger trigger = skillsManager.getTriggersRegistry().get(entry);
            if (trigger == null) continue;
            triggers.add(trigger);
        }

        var effectConfigs = config.hasPath("id") ? List.of(config) : config.getSubsectionList("effects");


        boolean directIDSpecified = config.hasPath("id");

        ChainExecutor executor = skillsManager.getChainExecutorsRegistry()
                .getByID(args.getStringOrNull("run-type"));

        var chain = compileChain(effectConfigs, executor, context, directIDSpecified);
        if(chain==null) return null;

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
                directIDSpecified
        );
    }

    @Nullable
    public Chain compileChain(Collection<Config> configs,
                              ChainExecutor executor,
                              ViolationContext context
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

            //@TODO
           /* val mutators = Mutators.compile(config.getSubsections("mutators"), context.with("mutators"))
           val filters = Filters.compile(config.getSubsection("filters"), context.with("filters"))
           */
            return new ChainElement<>(
                    plugin,
                    effect,
                    args,
                    compileData,
                    arguments,
                    conditions
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public EffectsRegistry(BossPlugin plugin) {
        this.plugin = plugin;
        register(new EffectIgnite(plugin));
    }


}
