package com.github.jitwxs.addax.core.convert.implicit;

import com.github.jitwxs.addax.provider.ConvertProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import com.google.protobuf.ProtocolMessageEnum;
import org.powermock.reflect.Whitebox;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 21:23
 */
public class StringToProtoEnumConvert implements ImplicitConvert<String, ProtocolMessageEnum> {
    @Override
    public <T> T convert(String source, Class<T> targetClass) {
        validParams(targetClass, ProtocolMessageEnum.class);

        final ConvertProvider provider = ProviderFactory.delegate(ConvertProvider.class);

        try {
            // 尝试使用 forNumber
            final int number = provider.convert(source, Integer.class);
            return Whitebox.invokeMethod(number, targetClass, "forNumber", new Class[]{int.class}, number);
        } catch (Exception e) {
            // TODO
        }

        return null;
    }
}
