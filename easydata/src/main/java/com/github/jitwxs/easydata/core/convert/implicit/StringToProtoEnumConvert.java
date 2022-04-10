package com.github.jitwxs.easydata.core.convert.implicit;

import com.github.jitwxs.easydata.common.exception.EasyDataConvertException;
import com.github.jitwxs.easydata.provider.ConvertProvider;
import com.github.jitwxs.easydata.provider.ProviderFactory;
import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.StringUtils;
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
            if (StringUtils.isNumeric(source)) {
                final int number = provider.convert(source, Integer.class);
                return Whitebox.invokeMethod(number, targetClass, "forNumber", new Class[]{int.class}, number);
            }

            return null;
        } catch (Exception e) {
            throw new EasyDataConvertException(e);
        }
    }
}
