package io.github.jitwxs.easydata.sample.bean;

import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.sample.message.MessageProto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.beans.Introspector;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-20 23:36
 */
public class FieldPropertyTest {
    @Test
    @SneakyThrows
    public void testForProto() {
        final Map<String, FieldProperty> properties = FieldProperty.newInstance(MessageProto.OrderEvaluate.class);

        assertNotNull(properties);
        assertEquals(MessageProto.OrderEvaluate.newBuilder().getDescriptorForType().getFields().size(), properties.size());
    }

    @Test
    @SneakyThrows
    public void testForJava() {
        final Map<String, FieldProperty> properties = FieldProperty.newInstance(OrderEvaluate.class);

        assertNotNull(properties);
        assertEquals(Introspector.getBeanInfo(OrderEvaluate.class, Object.class).getPropertyDescriptors().length, properties.size());
    }
}
