package com.github.jitwxs.easydata.sample.core.verify;

import com.github.jitwxs.easydata.core.mock.EasyMock;
import com.github.jitwxs.easydata.sample.sample.bean.OrderEvaluate;
import com.github.jitwxs.easydata.sample.sample.convert.OrderEvaluateConvert;
import com.github.jitwxs.easydata.sample.sample.enums.SexEnum;
import com.github.jitwxs.easydata.sample.sample.message.EnumProto;
import com.github.jitwxs.easydata.sample.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

import static com.github.jitwxs.easydata.core.verify.EasyVerify.with;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 不同简单类型的 Equals 比较
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 21:49
 */
public class NotSameSimpleClassITypeVerifyTest {
    @Test
    public void testCompareProtoAndBean1() {
        assertThrows(AssertionError.class, () -> with(EnumProto.SexEnum.MALE, SexEnum.MALE).verify());

        assertDoesNotThrow(() -> with(EnumProto.SexEnum.MALE, SexEnum.MALE).ignoreClassDiff().verify());
    }

    @Test
    public void testCompareProtoAndBean2() {
        final OrderEvaluate evaluate = EasyMock.run(OrderEvaluate.class);
        final MessageProto.OrderEvaluate evaluate1 = OrderEvaluateConvert.db2Proto(evaluate);

        with(evaluate, evaluate1).ignoreClassDiff().verify();
    }
}
