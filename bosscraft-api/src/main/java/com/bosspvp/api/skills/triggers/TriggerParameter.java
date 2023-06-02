package com.bosspvp.api.skills.triggers;

import lombok.Getter;

public enum TriggerParameter {
    PLAYER,
    VICTIM,
    BLOCK,
    EVENT,
    LOCATION(VICTIM, PLAYER),
    PROJECTILE,
    VELOCITY(PLAYER, VICTIM),
    ITEM(PLAYER, VICTIM),
    TEXT,
    VALUE;

    @Getter
    private final TriggerParameter[] inheritsFrom;
    TriggerParameter(TriggerParameter ... args){
        inheritsFrom = args;
    }
}
