package io.github.jitwxs.easydata.common.cache;

import io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnumCacheTest {
    enum TestEnum1 {
        JAVA,
        C
    }

    @Getter
    @AllArgsConstructor
    enum TestEnum2 {
        JAVA(1),
        C(2)
        ;

        private final int code;
    }

    @Test
    public void testEnumByOrdinal() {
        final EnumCache.EnumProperty property = EnumCache.tryGet(TestEnum1.class);

        assertNotNull(property);

        EasyVerify
                .with(new HashMap<Integer, TestEnum1>() {{
                    put(0, TestEnum1.JAVA);
                    put(1, TestEnum1.C);
                }}, property.getIdMap())
                .ignoreClassDiff(ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS)
                .verify();
    }

    @Test
    public void testEnumByCustom() {
        final EnumCache.EnumProperty property = EnumCache.tryGet(TestEnum2.class, one -> ((TestEnum2) one).getCode());

        assertNotNull(property);

        EasyVerify
                .with(new HashMap<Integer, TestEnum2>() {{
                    put(1, TestEnum2.JAVA);
                    put(2, TestEnum2.C);
                }}, property.getIdMap())
                .ignoreClassDiff(ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS)
                .verify();
    }
}