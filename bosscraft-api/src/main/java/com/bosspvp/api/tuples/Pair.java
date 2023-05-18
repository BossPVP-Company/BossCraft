package com.bosspvp.api.tuples;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private A first;

    /**
     * The second item in the tuple.
     */
    @NotNull
    private B second;

    /**
     * Create a pair of values.
     *
     * @param first  The first item in the pair.
     * @param second The second item in the pair.
     */
    public Pair(@NotNull final A first,
                @NotNull final B second) {
        this.first = first;
        this.second = second;
    }


    public A component1() {
        return first;
    }
    public B component2() {
        return second;
    }


    public @NotNull A getFirst() {
        return this.first;
    }
    public @NotNull B getSecond() {
        return this.second;
    }


    public void setFirst(@NotNull final A first) {
        this.first = first;
    }
    public void setSecond(@NotNull final B second) {
        this.second = second;
    }
}
