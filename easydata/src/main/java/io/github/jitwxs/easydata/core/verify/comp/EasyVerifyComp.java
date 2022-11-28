package io.github.jitwxs.easydata.core.verify.comp;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.util.DoubleComparator;
import org.assertj.core.util.FloatComparator;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * @since 1.11
 * @date 2022-11-27 22:21
 */
public class EasyVerifyComp {
    /**
     * Allows to register a comparator to compare the fields with the given type. A typical usage is for comparing double/float fields with a given precision.
     * Comparators registered with this method have less precedence than comparators registered with withComparatorForFields(Comparator, String...) or BiPredicate registered with withEqualsForFields(BiPredicate, String...).
     * Note that registering a Comparator for a given type will override the previously registered BiPredicate/Comparator (if any).
     *
     * @param anAssert         RecursiveComparisonAssert
     * @param precisionConfigs precisionConfigs
     */
    public static RecursiveComparisonAssert usingComparatorForType(RecursiveComparisonAssert anAssert, final Map<Class<?>, Object> precisionConfigs) {
        for (Map.Entry<Class<?>, Object> entry : precisionConfigs.entrySet()) {
            final Class<?> type = entry.getKey();

            final Comparator comparator = EasyVerifyComp.getComparator(Optional.of(entry.getValue()), type);

            if (comparator != null) {
                anAssert = anAssert.withComparatorForType(comparator, type);
            }
        }

        return anAssert;
    }

    /**
     * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
     * The custom comparator is bound to assertion instance, meaning that if a new assertion instance is created, the default comparison strategy will be used.
     *
     * @param anAssert         extends AbstractAssert
     * @param target           comparator type
     * @param precisionConfigs precisionConfigs
     */
    public static <T extends AbstractAssert> T usingComparator(T anAssert, final Class<?> target, final Map<Class<?>, Object> precisionConfigs) {
        final Comparator comparator = EasyVerifyComp.getComparator(Optional.ofNullable(precisionConfigs.get(target)), target);
        return comparator != null ? (T) anAssert.usingComparator(comparator) : anAssert;
    }

    private static Comparator getComparator(final Optional<Object> epsilon, final Class<?> target) {
        Comparator comparator = null;

        if (target == double.class || target == Double.class) {
            comparator = new DoubleComparator((Double) epsilon.orElse(0D));
        } else if (target == float.class || target == Float.class) {
            comparator = new FloatComparator((Float) epsilon.orElse(0F));
        } else if (target == int.class || target == Integer.class) {
            comparator = new IntegerComp((Integer) epsilon.orElse(0));
        } else if (target == long.class || target == Long.class) {
            comparator = new LongComp((Long) epsilon.orElse(0L));
        } else if (target == BigDecimal.class) {
            comparator = new BigDecimalComp((BigDecimal) epsilon.orElse(BigDecimal.ZERO));
        }

        return comparator;
    }
}
