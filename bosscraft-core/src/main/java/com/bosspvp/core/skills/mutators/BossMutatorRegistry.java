package com.bosspvp.core.skills.mutators;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registry;
import com.bosspvp.api.skills.mutators.Mutator;
import com.bosspvp.api.skills.mutators.MutatorBlock;
import com.bosspvp.api.skills.mutators.MutatorList;
import com.bosspvp.api.skills.mutators.MutatorRegistry;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import com.bosspvp.core.skills.mutators.types.MutatorLocationToPlayer;
import com.bosspvp.core.skills.mutators.types.MutatorLocationToVictim;
import com.bosspvp.core.skills.mutators.types.MutatorPlayerAsVictim;
import com.bosspvp.core.skills.mutators.types.MutatorVictimAsPlayer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BossMutatorRegistry extends Registry<Mutator<?>> implements MutatorRegistry {
    @Getter
    private final BossPlugin plugin;

    @Override
    public @NotNull MutatorList compile(@NotNull Collection<Config> configs, @NotNull ViolationContext context) {

        List<MutatorBlock<?>> list = new ArrayList<>();
        for (Config config : configs) {
            MutatorBlock<?> block = compile(config, context);
            if (block != null) {
                list.add(block);
            }
        }
        return new MutatorList(list);
    }

    @Override
    public @Nullable MutatorBlock<?> compile(@NotNull Config config, @NotNull ViolationContext context) {
        String mutatorID = config.getString("id");
        Mutator<?> mutator = get(mutatorID);

        if (mutator == null) {
            context.log(new ConfigViolation("id", "Invalid mutator ID specified: "+mutatorID));
            return null;
        }

        try {
            return makeBlock(mutator, config.getSubsection("args"), context.with("args"));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private<T> MutatorBlock<T> makeBlock(Mutator<T> mutator, Config config, ViolationContext context) throws Exception {
        if (!mutator.checkConfig(config, context)) {
            return null;
        }
        return new MutatorBlock<T>(mutator, config, mutator.makeCompileData(config, context));
    }

    @Override
    public @NotNull Registry<Mutator<?>> getRegistry() {
        return this;
    }

    public BossMutatorRegistry(BossPlugin plugin) {
        this.plugin = plugin;

        register(new MutatorPlayerAsVictim(plugin));
        register(new MutatorVictimAsPlayer(plugin));
        register(new MutatorLocationToPlayer(plugin));
        register(new MutatorLocationToVictim(plugin));
    }
}
