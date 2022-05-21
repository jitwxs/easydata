package io.github.jitwxs.easydata.sample.core.convert;

import io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import io.github.jitwxs.easydata.provider.ProviderFactory;
import io.github.jitwxs.easydata.sample.message.MessageProto;
import org.junit.jupiter.api.Test;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-21 18:30
 */
public class ProtoConvertTest {

    private final static ConvertProvider delegate = ProviderFactory.delegate(ConvertProvider.class);

    @Test
    public void builder2Message() {
        final MessageProto.OrderEvaluate.Builder protoBuilder = EasyMock.run(MessageProto.OrderEvaluate.Builder.class);

        final MessageProto.OrderEvaluate protoMessage = delegate.convert(protoBuilder, MessageProto.OrderEvaluate.class);

        EasyVerify.with(protoBuilder, protoMessage).ignoreClassDiff(ClassDiffVerifyStrategyEnum.VERIFY_SAME_FIELD).verify();
    }

    @Test
    public void message2Builder() {
        final MessageProto.OrderEvaluate protoMessage = EasyMock.run(MessageProto.OrderEvaluate.class);

        final MessageProto.OrderEvaluate.Builder protoBuilder = delegate.convert(protoMessage, MessageProto.OrderEvaluate.Builder.class);

        EasyVerify.with(protoMessage, protoBuilder).ignoreClassDiff(ClassDiffVerifyStrategyEnum.VERIFY_SAME_FIELD).verify();
    }
}
