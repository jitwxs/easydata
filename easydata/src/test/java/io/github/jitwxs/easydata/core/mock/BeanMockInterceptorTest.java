package io.github.jitwxs.easydata.core.mock;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author jitwxs
 * @see BeanMockInterceptor
 */
public class BeanMockInterceptorTest {
    @Test
    @DisplayName("跳过全部字段")
    public void testSkipAllField() {
        final MockConfig mockConfig = new MockConfig()
                .registerBeanMockerInterceptor(User.class, (fieldName, config) -> BeanMockInterceptor.MockRes.SKIP_MOCK);

        final User nullUser = User.builder()
                .age(null)
                .guid(null)
                .active(false)
                .longitude(0D)
                .tags(null)
                .build();

        User user = EasyMock.run(User.class);
        assertNotEquals(nullUser, user);

        user = EasyMock.run(User.class, mockConfig);
        assertEquals(nullUser, user);
    }

    @Test
    @DisplayName("自定义字段")
    public void testInterceptorField() {
        final MockConfig mockConfig = new MockConfig()
                .registerBeanMockerInterceptor(User.class, (fieldName, config) -> {
                    switch (fieldName) {
                        case "age":
                            return 99; // -> 99
                        case "active":
                            return null; // -> false
                        case "tags":
                            return BeanMockInterceptor.MockRes.CALL_MOCK;
                        default:
                            return BeanMockInterceptor.MockRes.SKIP_MOCK;
                    }
                });

        final User user = EasyMock.run(User.class, mockConfig);

        assertNotNull(user);

        assertEquals(99, user.getAge());
        assertNull(user.getGuid());
        assertFalse(user.isActive());
        assertEquals(0D, user.getLongitude());
        assertTrue(CollectionUtils.isNotEmpty(user.getTags()));
    }

    @Data
    @Builder
    static class User {
        private Integer age;

        private String guid;

        private boolean active;

        private double longitude;

        private List<String> tags;
    }
}