package com.bosspvp.api.skills.effects;

import com.bosspvp.api.BossAPI;
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

public class EffectsRegistry extends Registry<Effect<?>> {
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
        SkillsManager skillsManager = BossAPI.getInstance().getCorePlugin().getSkillsManager();

        //@TODO
        Config config = cfg.separatorAmbivalent();

        Config args = config.getSubsection("args");

        var arguments = skillsManager.getEffectArgumentsRegistry().compile(args, context.with("args"));
        var conditions = skillsManager.getConditionsRegistry().compile(config.getSubsections("conditions"), context.with("conditions"));

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

        //@TODO
        val effectConfigs = if (config.has("id")) {
            listOf(config)
        } else {
            config.getSubsections("effects")
        }

        boolean directIDSpecified = config.hasPath("id");

        ChainExecutor executor = skillsManager.getChainExecutorsRegistry()
                .getByID(args.getStringOrNull("run-type"));

        val chain = compileChain(effectConfigs, executor, context, directIDSpecified) ?:return null

        val permanentEffects = chain.filter {
            it.effect.isPermanent
        }
        val triggeredEffects = chain.filterNot {
            it.effect.isPermanent
        }

        if (triggers.isNotEmpty() && permanentEffects.isNotEmpty()) {
            context.log(
                    new ConfigViolation(
                            "triggers", "Triggers are not allowed on permanent " +
                            "effects: ${permanentEffects.joinToString(", ") { it.effect.id }}!"
                    )
            )
            return null
        }

        if (triggers.isEmpty() && chain.any {
            !it.effect.isPermanent
        }){
            context.log(
                    ConfigViolation(
                            "triggers", "You must specify at least one trigger for " +
                                    "triggered effects: ${triggeredEffects.joinToString(", ") { it.effect.id }}!"
                    )
            )
            return null
        }

        var isInvalid = false
        for (element in chain) {
            for (trigger in triggers) {
                if (!element.effect.supportsTrigger(trigger)) {
                    isInvalid = true
                    context.log(
                            ConfigViolation(
                                    "triggers",
                                    "${element.effect.id} does not support trigger ${trigger.id}"
                            )
                    )
                }
            }

            if (!triggers.all {
                element.effect.supportsTrigger(it)
            }){
                isInvalid = true
            }
        }

        if (isInvalid) {
            return null
        }

        return new EffectBlock(
                UUID.randomUUID(),
                args,
                chain,
                triggers,
                arguments,
                conditions,
                mutators,
                filters,
                directIDSpecified
        )
    }

    @Nullable
    public Chain compileChain(Collection<Config> configs,
                              ChainExecutor executor,
                              ViolationContext context,
                              ) {
        return compileChain(configs, executor, context, false);
    }

    @Nullable
    private Chain compileChain(Collection<Config> configs,
                               ChainExecutor executor,
                               ViolationContext context,
                               boolean directIDSpecified// If it's configured with 'id', rather than 'effects'
    ) {
        return null;
        //@TODO
        /*val elements = configs.stream().map(
                it-> it.separatorAmbivalent() }.mapNotNull { compileElement(it, context)
    )

        if ((elements.size > 1 || !directIDSpecified) && elements.any { it.effect.isPermanent }) {
            context.log(
                    ConfigViolation(
                            "effects",
                            "Permanent effects (${
                            elements.filter { it.effect.isPermanent }.joinToString(", ") { it.effect.id }
        }) are not allowed in chains!")
            )
        return null
    }

        return Chain(elements, executor)*/
    }
    /*

    private fun compileElement(config: Config, context: ViolationContext): ChainElement<*>? {
        val id = config.getString("id")
        val effect = this.get(id)

        if (effect == null) {
        context.log(ConfigViolation("id", "Invalid effect ID specified: ${id}!"))
        return null
        }

        *//*

        This might be useful in the future to warn people about deprecated effects,
        but currently it would lead to a shit ton of bug reports, especially for
        run_chain_inline.

        val deprecation = effect::class.java.annotations
            .firstOrNull { it::class.java == Deprecated::class.java }
            ?.let { it as? Deprecated }?.message

        if (deprecation != null) {
            context.log(ConfigViolation("id", "Effect $id is deprecated: $deprecation"))
            // Continue anyway
        }

         *//*

        return makeElement(effect, config, context)
        }

private fun <T> makeElement(
        effect: Effect<T>,
        config: Config,
        context: ViolationContext
        ): ChainElement<T>? {
        val args = config.getSubsection("args")

        if (!effect.checkConfig(args, context.with("args"))) {
        return null
        }

        val compileData = effect.makeCompileData(args, context.with("args"))

        val arguments = EffectArguments.compile(args, context.with("args"))
        val conditions = Conditions.compile(config.getSubsections("conditions"), context.with("conditions"))
        val mutators = Mutators.compile(config.getSubsections("mutators"), context.with("mutators"))
        val filters = Filters.compile(config.getSubsection("filters"), context.with("filters"))

        return ChainElement(
        effect,
        args,
        compileData,
        arguments,
        conditions,
        mutators,
        filters
        )
        }*/
    public EffectsRegistry() {
        register(new EffectIgnite());
    }


}
