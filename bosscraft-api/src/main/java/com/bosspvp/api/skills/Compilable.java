package com.bosspvp.api.skills;

import com.bosspvp.api.config.BossConfig;
import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class Compilable<T> implements Registrable {

    @Getter
    private final String id;
    @Getter
    private final ConfigArgument.Arguments arguments;
    public Compilable(@NotNull String id, @NotNull ConfigArgument.Arguments arguments){
        this.id = id;
        this.arguments = arguments;

    }


    public T makeCompileData(BossConfig config, ViolationContext context) {

        return null;
    }

    public boolean checkConfig(@NotNull BossConfig config, ViolationContext context) {
        var violations = arguments.test(config.asSection());

        for (ConfigViolation violation : violations) {
            context.log(this, violation);
        }

        return violations.isEmpty();
    }

    public interface Compiled<T>{
        BossConfig getConfig();
        T getCompileData();
    }
}
