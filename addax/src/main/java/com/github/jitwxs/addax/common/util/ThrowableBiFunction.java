package com.github.jitwxs.addax.common.util;

import java.util.function.BiFunction;

/**
 * Throwable <code>BiFunction</code>
 *
 * @param <T> input parameter type T
 * @param <U> input parameter type U
 * @param <R> output return type
 * @author PARK Yong Seo
 * @see java.util.function.Function
 * @since 0.1
 */
@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {
    /**
     * @param t input parameter type T
     * @param u input parameter type U
     * @return output return
     * @throws Exception
     * @see java.util.function.Function
     */
    R apply(T t, U u) throws Exception;

    /**
     * throws Exception lambda to throws RuntimeException lambda
     *
     * @param biFunction
     * @return
     * @since 0.6
     */
    public static <T, U, R> BiFunction<T, U, R> runtime(ThrowableBiFunction<T, U, R> biFunction) {
        return (t, u) -> {
            try {
                return biFunction.apply(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}