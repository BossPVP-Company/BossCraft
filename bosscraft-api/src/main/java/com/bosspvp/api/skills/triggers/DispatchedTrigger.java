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

    public static Builder builder(){
        return new Builder();
    }
    public Builder copyToBuilder(){
        return new Builder()
                .player(player)
                .trigger(trigger)
                .data(data)
                .placeholders(placeholders);
    }
    public static class Builder{
        private Player player;
        private Trigger trigger;
        private TriggerData data;
        private List<InjectablePlaceholder> placeholders;

        public Builder(){
            this.placeholders = new ArrayList<>();
        }

        public Builder player(Player player){
            this.player = player;
            return this;
        }
        public Builder trigger(Trigger trigger){
            this.trigger = trigger;
            return this;
        }
        public Builder data(TriggerData data){
            this.data = data;
            return this;
        }
        public Builder placeholders(List<InjectablePlaceholder> placeholders){
            this.placeholders = placeholders;
            return this;
        }
        public Builder placeholder(InjectablePlaceholder placeholder){
            this.placeholders.add(placeholder);
            return this;
        }
        public Builder placeholder(String placeholder, String value){
            this.placeholders.add(new StaticPlaceholder(placeholder,()-> value));
            return this;
        }
        public Builder placeholder(String placeholder, Supplier<String> value){
            this.placeholders.add(new StaticPlaceholder(placeholder,value));
            return this;
        }
        public DispatchedTrigger build(){
            return new DispatchedTrigger(player,trigger,data,placeholders);
        }
    }
}
