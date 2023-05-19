package com.bosspvp.api.tuples;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Two values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 */
public class Pair<A, B> {
    /**
     * The first item in the tuple.
     */
    @Nullable
    private A first;

    /**
     * The second item in the tuple.
     */
    @Nullable
    private B second;

    /**
     * Create a pair of values.
     *
     * @param first  The first item in the pair.
     * @param second The second item in the pair.
     */
    public Pair(@Nullable final A first,
                @Nullable final B second) {
        this.first = first;
        this.second = second;
    }


    public A component1() {
        return first;
    }
    public B component2() {
        return second;
    }


    public @Nullable A getFirst() {
        return this.first;
    }
    public @Nullable B getSecond() {
        return this.second;
    }


    public void setFirst(@Nullable final A first) {
        this.first = first;
    }
    public void setSecond(@Nullable final B second) {
        this.second = second;
    }
}
