package io.github.jitwxs.easydata.sample.bean;

import com.google.protobuf.Descriptors;
import io.github.jitwxs.easydata.common.bean.FieldProperty;
import io.github.jitwxs.easydata.sample.message.MessageProto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-20 23:36
 */
public class FieldPropertyTest {
    @Test
    public void testForProto() throws IntrospectionException {
        final Map<String, FieldProperty> properties = FieldProperty.newInstance(MessageProto.OrderEvaluate.class);

        assertNotNull(properties);
        assertEquals(MessageProto.OrderEvaluate.newBuilder().getDescriptorForType().getFields().size(), properties.size());
    }

    @Test
    @DisplayName("测试 protobuf oneof 语法")
    public void testForProtoOneof() throws IntrospectionException {
        final Map<String, FieldProperty> properties = FieldProperty.newInstance(MessageProto.OrderEvaluateOperation.Builder.class);

        assertNotNull(properties);

        final List<Descriptors.FieldDescriptor> descriptorList = MessageProto.OrderEvaluateOperation.newBuilder().getDescriptorForType().getFields();

        assertEquals(descriptorList.size(), properties.size());
    }

    @Test
    public void testForJava() throws IntrospectionException {
        final Map<String, FieldProperty> properties = FieldProperty.newInstance(OrderEvaluate.class);

        assertNotNull(properties);
        assertEquals(Introspector.getBeanInfo(OrderEvaluate.class, Object.class).getPropertyDescriptors().length, properties.size());
    }
}
