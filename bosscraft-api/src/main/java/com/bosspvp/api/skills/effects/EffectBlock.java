package com.bosspvp.api.skills.effects;

import com.bosspvp.api.config.Config;
import com.bosspvp.api.skills.conditions.ConditionList;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentList;
import com.bosspvp.api.skills.triggers.DispatchedTrigger;

import java.util.UUID;

public class EffectBlock extends ElementLike{
    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public Config getConfig() {
        return null;
    }

    @Override
    public EffectArgumentList getArguments() {
        return null;
    }

    @Override
    public ConditionList getConditions() {
        return null;
    }

    @Override
    public boolean isSupportDelay() {
        return false;
    }

    @Override
    protected boolean doTrigger(DispatchedTrigger trigger) {
        return false;
    }


    public int getWeight(){

    }
}
