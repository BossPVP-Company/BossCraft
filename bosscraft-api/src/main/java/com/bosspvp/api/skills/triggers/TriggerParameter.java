package com.bosspvp.api.skills.triggers;

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

    private TriggerParameter[] inheritsFrom;
    TriggerParameter(TriggerParameter ... args){
        inheritsFrom = args;
    }
}
