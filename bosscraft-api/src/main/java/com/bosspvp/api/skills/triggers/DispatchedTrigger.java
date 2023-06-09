package com.bosspvp.api.skills.triggers;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record DispatchedTrigger(@NotNull Player player,
                                @NotNull Trigger trigger,
                                @NotNull TriggerData data,
                                @NotNull List<InjectablePlaceholder> placeholders) {

    public DispatchedTrigger(@NotNull Player player,
                             @NotNull Trigger trigger,
                             @NotNull TriggerData data){
        this(player,trigger,data,new ArrayList<>());
    }

    public void  addPlaceholder(@NotNull String placeholder,
                                 @NotNull String value){
        placeholders.add(new StaticPlaceholder(placeholder,()-> value));
    }
    public void  addPlaceholder(@NotNull InjectablePlaceholder placeholder){
        placeholders.add(placeholder);
    }
    public void  addPlaceholderDynamic(@NotNull String placeholder,
                                       @NotNull Supplier<String> value){
        placeholders.add(new StaticPlaceholder(placeholder,value));
    }
    public DispatchedTrigger inheritPlaceholders(DispatchedTrigger other) {
        placeholders.addAll(other.placeholders);
        return this;
    }

    public void generateTriggerPlaceholders(TriggerData data){
            for(TriggerPlaceholder placeholder : BossAPI.getInstance().getCorePlugin().getSkillsManager()
                    .getTriggerPlaceholdersRegistry().getRegistry().values()){

                placeholders.addAll(placeholder.createPlaceholders(data));
            }
    }
    public void generateTriggerPlaceholders(){
        generateTriggerPlaceholders(this.data);
    }
}
