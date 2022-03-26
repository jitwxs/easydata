package com.github.jitwxs.addax.core.verify;

import com.github.jitwxs.addax.core.mock.Mock;
import com.github.jitwxs.addax.sample.bean.OrderEvaluate;
import com.github.jitwxs.addax.sample.convert.OrderEvaluateConvert;
import com.github.jitwxs.addax.sample.enums.SexEnum;
import com.github.jitwxs.addax.sample.message.EnumProto;
import com.github.jitwxs.addax.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

/**
 * 不同简单类型的 Equals 比较
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 21:49
 */
public class NotSameSimpleClassITypeVerifyTest {
    @Test
    public void testCompareProtoAndBean1() {
        new Verify(EnumProto.SexEnum.MALE).ignoreClassDiff().asserts(SexEnum.MALE);
    }

    @Test
    public void testCompareProtoAndBean2() {
        final OrderEvaluate evaluate = Mock.run(OrderEvaluate.class);
        final MessageProto.OrderEvaluate evaluate1 = OrderEvaluateConvert.db2Proto(evaluate);

        new Verify(evaluate).ignoreClassDiff().asserts(evaluate1);
    }
}
