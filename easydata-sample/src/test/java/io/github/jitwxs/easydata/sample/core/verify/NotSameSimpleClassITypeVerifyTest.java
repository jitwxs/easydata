package io.github.jitwxs.easydata.sample.core.verify;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.sample.bean.OrderEvaluate;
import io.github.jitwxs.easydata.sample.convert.OrderEvaluateConvert;
import io.github.jitwxs.easydata.sample.enums.SexEnum;
import io.github.jitwxs.easydata.sample.message.EnumProto;
import io.github.jitwxs.easydata.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

import static io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS;
import static io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum.VERIFY_SAME_FIELD;
import static io.github.jitwxs.easydata.core.verify.EasyVerify.with;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 不同简单类型的 Equals 比较
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 21:49
 */
public class NotSameSimpleClassITypeVerifyTest {
    /**
     * proto 枚举类 vs java 枚举类
     */
    @Test
    public void testCompareProtoAndBean1() {
        assertThrows(AssertionError.class, () -> with(EnumProto.SexEnum.MALE, SexEnum.MALE).verify());

        assertDoesNotThrow(() -> with(EnumProto.SexEnum.MALE, SexEnum.MALE).ignoreClassDiff(CONVERT_SAME_CLASS).verify());
    }

    /**
     * java 对象 vs proto 对象，两边字段有缺失
     */
    @Test
    public void testCompareProtoAndBean2() {
        final OrderEvaluate evaluate = EasyMock.run(OrderEvaluate.class);
        final MessageProto.OrderEvaluate evaluate1 = OrderEvaluateConvert.db2Proto(evaluate);

        assertThrows(AssertionError.class, () -> with(evaluate, evaluate1).ignoreClassDiff(CONVERT_SAME_CLASS).verify());
        assertDoesNotThrow(() -> with(evaluate, evaluate1).ignoreClassDiff(VERIFY_SAME_FIELD).verify());
    }

    /**
     * proto 对象 vs java 对象，两边字段有缺失
     */
    @Test
    public void testCompareProtoAndBean3() {
        final MessageProto.OrderEvaluate evaluate = EasyMock.run(MessageProto.OrderEvaluate.class, new MockConfig().contrastClass(OrderEvaluate.class));
        final OrderEvaluate evaluate1 = OrderEvaluateConvert.proto2Db(evaluate);

        assertThrows(AssertionError.class, () -> with(evaluate, evaluate1).ignoreClassDiff(CONVERT_SAME_CLASS).verify());
        assertDoesNotThrow(() -> with(evaluate, evaluate1).ignoreClassDiff(VERIFY_SAME_FIELD).verify());
    }

    /**
     * java 对象 vs proto builder 对象，两边字段有缺失
     */
    @Test
    public void testCompareProtoAndBean4() {
        final OrderEvaluate evaluate = EasyMock.run(OrderEvaluate.class);
        final MessageProto.OrderEvaluate.Builder evaluate1 = OrderEvaluateConvert.db2ProtoBuilder(evaluate);

        assertThrows(AssertionError.class, () -> with(evaluate, evaluate1).ignoreClassDiff(CONVERT_SAME_CLASS).verify());
        assertDoesNotThrow(() -> with(evaluate, evaluate1).ignoreClassDiff(VERIFY_SAME_FIELD).verify());
    }

    /**
     * proto builder 对象 vs proto builder 对象
     */
    @Test
    public void testCompareProtoAndBean5() {
        final OrderEvaluate evaluate = EasyMock.run(OrderEvaluate.class);

        final MessageProto.OrderEvaluate.Builder evaluate1 = OrderEvaluateConvert.db2ProtoBuilder(evaluate);
        final MessageProto.OrderEvaluate.Builder evaluate2 = OrderEvaluateConvert.db2ProtoBuilder(evaluate);

        with(evaluate1, evaluate2).verify();
    }

    /**
     * proto message 对象 vs proto message 对象
     */
    @Test
    public void testCompareProtoAndBean6() {
        final OrderEvaluate evaluate = EasyMock.run(OrderEvaluate.class);

        final MessageProto.OrderEvaluate evaluate1 = OrderEvaluateConvert.db2Proto(evaluate);
        final MessageProto.OrderEvaluate evaluate2 = OrderEvaluateConvert.db2Proto(evaluate);

        with(evaluate1, evaluate2).verify();
    }
}
