package io.github.jitwxs.easydata.sample.provider;

import io.github.jitwxs.easydata.core.verify.EasyVerify;
import io.github.jitwxs.easydata.provider.ConvertProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 12:31
 */
public class ConvertProviderTest {
    @Test
    @SneakyThrows
    public void processFieldValue() {
        final Method method = Whitebox.getMethod(ConvertProvider.class, "processFieldValue", Object.class, Class.class);

        EasyVerify.with(0, method.invoke(null, null, int.class)).verify();
        EasyVerify.isNull(method.invoke(null, null, Integer.class));
    }
}
