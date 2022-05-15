package io.github.jitwxs.easydata.common.function;

@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> {

    R apply(T t, U u) throws Throwable;

    default <V> ThrowableBiFunction<T, U, V> andThen(ThrowableFunction<? super R, ? extends V> a) {
        return (t, u) -> a.apply(apply(t, u));
    }

    static <T, U, R> R execute(T t, U u, ThrowableBiFunction<T, U, R> function) throws IllegalArgumentException {
        try {
            return function.apply(t, u);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }
}