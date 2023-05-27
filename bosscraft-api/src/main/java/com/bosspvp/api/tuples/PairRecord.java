package com.bosspvp.api.tuples;

/**
 * Two not null values.
 *
 * @param <A> The first value type.
 * @param <B> The second value type.
 */
public record PairRecord<A, B>(A first, B second) {
}
