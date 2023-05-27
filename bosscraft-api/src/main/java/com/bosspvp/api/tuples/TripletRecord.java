package com.bosspvp.api.tuples;

/**
 * Three not null values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 * @param <C> The third value type.
 */
public record TripletRecord<A, B, C>(A first, B second, C third) {
}
