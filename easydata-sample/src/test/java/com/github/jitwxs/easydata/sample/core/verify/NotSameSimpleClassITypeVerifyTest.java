package com.github.jitwxs.easydata.sample.core.verify;

import com.github.jitwxs.easydata.sample.core.mock.EasyMock;
import com.github.jitwxs.easydata.sample.sample.bean.OrderEvaluate;
import com.github.jitwxs.easydata.sample.sample.convert.OrderEvaluateConvert;
import com.github.jitwxs.easydata.sample.sample.enums.SexEnum;
import com.github.jitwxs.easydata.sample.sample.message.EnumProto;
import com.github.jitwxs.easydata.sample.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

import static com.github.jitwxs.easydata.sample.core.verify.EasyVerify.go;

/**
 * 不同简单类型的 Equals 比较
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 21:49
 */
public class NotSameSimpleClassITypeVerifyTest {
    @Test
    public void testCompareProtoAndBean1() {
        go(EnumProto.SexEnum.MALE, SexEnum.MALE).ignoreClassDiff().run();
    }

    @Test
    public void testCompareProtoAndBean2() {
        final OrderEvaluate evaluate = EasyMock.run(OrderEvaluate.class);
        final MessageProto.OrderEvaluate evaluate1 = OrderEvaluateConvert.db2Proto(evaluate);

        go(evaluate, evaluate1).ignoreClassDiff().run();
    }
}
