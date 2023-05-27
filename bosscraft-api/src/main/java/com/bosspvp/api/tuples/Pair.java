package com.bosspvp.api.tuples;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * Two nullable values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 */
@AllArgsConstructor
public class Pair<A, B> {
    /**
     * The first item in the tuple.
     */
    @Nullable @Getter @Setter
    private A first;

    /**
     * The second item in the tuple.
     */
    @Nullable @Getter @Setter
    private B second;
}
