package com.bosspvp.api.skills.effects;

import lombok.Getter;

public enum RunOrder {
    /** Run absolutely first. Only use in exceptional circumstances. */
    START(-5),

    /** Run early. */
    EARLY(-2),

    /** Run in the middle (default) */
    NORMAL(0),

    /** Run late. */
    LATE(2),

    /** Run right at the end. Only use in exceptional circumstances. */
    END(5);

    @Getter
    private int weight;

    RunOrder(int weight){
        this.weight = weight;
    }
}
