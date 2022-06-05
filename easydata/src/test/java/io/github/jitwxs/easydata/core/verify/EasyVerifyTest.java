package io.github.jitwxs.easydata.core.verify;

import com.google.common.collect.Sets;
import io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EasyVerifyTest {
    @Test
    public void testStringSetVerifyWithIgnoreElements() {
        final Set<String> except = Sets.newHashSet("a", "b", "c");
        final Set<String> actual = Sets.newHashSet("b", "c");

        EasyVerify
                .with(except, actual)
                .ignoreElements("a")
                .ignoreClassDiff(ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS)
                .verify();
    }

    @Test
    public void testStringMapVerifyWithIgnoreElements() {
        final Map<String, Double> except = new HashMap<String, Double>() {{
            put("a", 12.1);
            put("b", 13.1);
            put("c", 14.1);
        }};
        final Map<String, Double> actual = new HashMap<String, Double>() {{
            put("a", 2.1);
            put("b", 13.1);
            put("c", 14.1);
        }};

        EasyVerify
                .with(except, actual)
                .ignoreElements("a")
                .ignoreClassDiff(ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS)
                .verify();
    }
}