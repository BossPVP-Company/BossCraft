package com.bosspvp.core.skills.effects.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.Compilable;
import com.bosspvp.api.skills.effects.Effect;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.TriggerParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EffectSendMessage extends Effect<Compilable.NoCompileData> {
    public EffectSendMessage(@NotNull BossPlugin plugin) {
        super(plugin, "send_message");
        setArguments(it->{
            it.require("message","You must specify the message to send!");
        });
    }

    @Override
    public Set<TriggerParameter> getParameters() {
        return Set.of(TriggerParameter.PLAYER);
    }

    @Override
    protected boolean onTrigger(Config config, TriggerData data, NoCompileData compileData) {
        if(data.player()==null) return false;
        List<String> messages = config.getFormattedStringListOrNull("message", data.toPlaceholderContext(config));
        if(messages==null || messages.isEmpty()) messages = List.of(config.getFormattedString("message",data.toPlaceholderContext(config)));
        messages = messages.stream().map(it->it.replace("%player%",data.player().getName())).toList();
        data.player().sendMessage(messages.toArray(new String[0]));
        //@TODO
        /*  val actionBar = config.getBool("action_bar")

        if (actionBar) {
            player.asAudience().sendActionBar(messages.first().toComponent())
        } else {
            for (s in messages) {
                player.asAudience().sendMessage(s.toComponent())
            }
        }*/
        return true;
    }
}
