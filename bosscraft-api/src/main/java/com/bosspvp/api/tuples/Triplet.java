package com.bosspvp.api.tuples;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    private A first;

    /**
     * The second item in the tuple.
     */
    @Nullable
    private B second;

    /**
     * The third item in the tuple.
     */
    @Nullable
    private C third;

    /**
     * Create a triplet of values.
     *
     * @param first  The first item in the pair.
     * @param second The second item in the pair.
     * @param third The third item in the pair.
     */
    public Triplet(@Nullable final A first,
                   @Nullable final B second,
                   @Nullable final C third) {
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


    public @Nullable A getFirst() {
        return this.first;
    }
    public @Nullable B getSecond() {
        return this.second;
    }
    public @Nullable C getThird() {
        return this.third;
    }


    public void setFirst(@Nullable final A first) {
        this.first = first;
    }
    public void setSecond(@Nullable final B second) {
        this.second = second;
    }
    public void setThird(@Nullable final C third) {
        this.third = third;
    }
}
