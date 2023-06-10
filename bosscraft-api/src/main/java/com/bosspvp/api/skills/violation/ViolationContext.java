package com.bosspvp.api.skills.violation;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.skills.Compilable;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViolationContext {
    @Getter
    private final BossPlugin plugin;

    @Getter
    private final List<String> parents;

    private final boolean log;
    public ViolationContext(@NotNull BossPlugin plugin){
       this(plugin, new ArrayList<>(),true);
    }
    public ViolationContext(@NotNull BossPlugin plugin, @NotNull String context){
        this(plugin, List.of(context),true);
    }
    public ViolationContext(@NotNull BossPlugin plugin,
                            @NotNull List<String> parents,
                            boolean log){
        this.plugin = plugin;
        this.parents = parents;
        this.log = log;
    }

    /**
     * Copy the violation context with an extra added context.
     */
    public ViolationContext with(String context) {
        List<String> list = new ArrayList<>(parents);
        list.add(context);
        return new ViolationContext(plugin, list,log);
    }

    /**
     * Log a violation.
     */
    public void log(Compilable<?> property, ConfigViolation violation) {
        if(!log) return;

        plugin.getLogger().warning(
                String.format(
                        """
                        Invalid configuration for %s found at %s:
                        (Cause) Argument %s
                        (Reason) %s
                        
                        """,
                       property.getId(), this, violation.param(), violation.message())
        );
    }

    /**
     * Log a violation.
     */
    public void log(ConfigViolation violation) {
        if(!log) {
            return;
        }
        plugin.getLogger().warning(
                String.format(
                       """
                       Invalid configuration found at %s:
                       (Cause) Argument %s
                       (Reason) %s
                       
                       """,
                this, violation.param(), violation.message())
        );
    }
    @Override
    public String toString() {
        return parents.toString();
    }
}
