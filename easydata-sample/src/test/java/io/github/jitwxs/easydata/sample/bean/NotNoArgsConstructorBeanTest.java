package io.github.jitwxs.easydata.sample.bean;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotNoArgsConstructorBeanTest {
    @Test
    public void testMock() {
        final EasyDataException exception = assertThrows(EasyDataException.class, () -> EasyMock.run(NotNoArgsConstructorBean.class));
        assertTrue(exception.getMessage().contains("failed create object"));

        final NotNoArgsConstructorBean except = new NotNoArgsConstructorBean(1, 2D);
        final NotNoArgsConstructorBean actual = assertDoesNotThrow(() -> EasyMock.run(NotNoArgsConstructorBean.class, new MockConfig().addBeanCache(except)));
        EasyVerify.with(except, actual).verify();
    }
}