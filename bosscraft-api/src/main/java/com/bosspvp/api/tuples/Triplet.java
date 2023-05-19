package com.bosspvp.api.tuples;

import org.jetbrains.annotations.NotNull;

/**
 * Three values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 * @param <C> The third value type.
 */
public class Triplet<A,B,C> {

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
     * The third item in the tuple.
     */
    @NotNull
    private C third;

    /**
     * Create a triplet of values.
     *
     * @param first  The first item in the pair.
     * @param second The second item in the pair.
     * @param third The third item in the pair.
     */
    public Triplet(@NotNull final A first,
                   @NotNull final B second,
                   @NotNull final C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }


    public A component1() {
        return first;
    }
    public B component2() {
        return second;
    }
    public C component3() {
        return third;
    }


    public @NotNull A getFirst() {
        return this.first;
    }
    public @NotNull B getSecond() {
        return this.second;
    }
    public @NotNull C getThird() {
        return this.third;
    }


    public void setFirst(@NotNull final A first) {
        this.first = first;
    }
    public void setSecond(@NotNull final B second) {
        this.second = second;
    }
    public void setThird(@NotNull final C third) {
        this.third = third;
    }
}
