package com.bosspvp.api.skills;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.skills.violation.ConfigViolation;
import com.bosspvp.api.skills.violation.ViolationContext;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class Compilable<T> implements Registrable {

    @Getter
    private final String id;
    @Getter
    private ConfigArgument.Arguments arguments = new ConfigArgument.Arguments(new ArrayList<>());
    public Compilable(@NotNull String id){
        this.id = id;

    }

    protected void setArguments(@NotNull Consumer<ConfigArgument.ConfigArgumentsBuilder> arguments){
        ConfigArgument.ConfigArgumentsBuilder builder = new ConfigArgument.ConfigArgumentsBuilder();
        arguments.accept(builder);
        this.arguments = builder.build();
    }
    protected void setArguments(@NotNull ConfigArgument.Arguments arguments){
        this.arguments = arguments;
    }

    public T makeCompileData(Config config, ViolationContext context) throws Exception{
        try {
            var empty = ((T) new NoCompileData());
            return empty;
        }catch (ClassCastException e){
            throw new Exception(
                    "You must override makeCompileData or use NoCompileData as the type!"
            );
        }

    }

    public boolean checkConfig(@NotNull Config config, ViolationContext context) {
        var violations = arguments.test(config);

        for (ConfigViolation violation : violations) {
            context.log(this, violation);
        }

        return violations.isEmpty();
    }

    public interface Compiled<T>{
        Config getConfig();
        T getCompileData();
    }

    /**
     * Empty data container for compiler
     */
    public class NoCompileData{

    }
}
