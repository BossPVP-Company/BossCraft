package com.bosspvp.api.skills;

import com.bosspvp.api.skills.conditions.ConditionsRegistry;
import com.bosspvp.api.skills.effects.EffectsRegistry;
import com.bosspvp.api.skills.effects.arguments.EffectArgumentsRegistry;
import com.bosspvp.api.skills.effects.executors.ChainExecutorRegistry;
import com.bosspvp.api.skills.holder.HolderManager;
import com.bosspvp.api.skills.triggers.TriggersRegistry;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholdersRegistry;

public interface SkillsManager {

    TriggersRegistry getTriggersRegistry();
    TriggerPlaceholdersRegistry getTriggerPlaceholdersRegistry();
    ConditionsRegistry getConditionsRegistry();

    EffectsRegistry getEffectsRegistry();
    EffectArgumentsRegistry getEffectArgumentsRegistry();
    ChainExecutorRegistry getChainExecutorsRegistry();
    HolderManager getHolderManager();

}
