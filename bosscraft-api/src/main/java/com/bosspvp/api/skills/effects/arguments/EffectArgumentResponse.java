package com.bosspvp.api.skills.effects.arguments;

import java.util.List;

public record EffectArgumentResponse(boolean wasMet,
                                     List<EffectArgumentBlock<?>> met,
                                     List<EffectArgumentBlock<?>> notMet) {
}
