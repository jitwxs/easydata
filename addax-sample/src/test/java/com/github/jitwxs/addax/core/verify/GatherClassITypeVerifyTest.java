package com.github.jitwxs.addax.core.verify;

import com.github.jitwxs.addax.LoggerStarter;
import com.github.jitwxs.addax.common.bean.MockConfig;
import com.github.jitwxs.addax.common.enums.MockStringEnum;
import com.github.jitwxs.addax.core.mock.Mock;
import com.github.jitwxs.addax.sample.bean.UserInfo;
import com.github.jitwxs.addax.sample.message.EnumProto;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 相同复杂类型的 Equals 比较
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 18:46
 */
@Slf4j
public class GatherClassITypeVerifyTest extends LoggerStarter {
    @Test
    @DisplayName("Set集合")
    public void testSet() {
        final HashSet<Object> set1 = new HashSet<>(), set2 = new HashSet<>();

        IntStream.range(0, 100).boxed().forEach(i -> {
            set1.add(i);
            set2.add(i);
        });

        new Verify(set1).asserts(set2);
    }

    @Test
    @DisplayName("Set和List集合")
    public void testSetList() {
        final Set<String> a = new HashSet<>();
        final List<String> b = new ArrayList<>();

        final MockConfig config = new MockConfig().setStringEnum(MockStringEnum.UUID);

        IntStream.range(0, 5).boxed().forEach(i -> {
            final String str = Mock.run(String.class, config);
            a.add(str);
            b.add(str);
        });

        Assertions.assertThrows(AssertionError.class, () -> new Verify(a).asserts(b));
        Assertions.assertDoesNotThrow(() -> new Verify(a).ignoreClassDiff().asserts(b));
    }

    @Test
    @DisplayName("枚举集合")
    public void testEnumCollection() {
        // arrayList
        final Collection<EnumProto.SexEnum> enums1 = Lists.newArrayList(EnumProto.SexEnum.MALE, EnumProto.SexEnum.FEMALE);
        // linkedList
        final Collection<EnumProto.SexEnum> enums2 = new LinkedList<>(Arrays.asList(EnumProto.SexEnum.MALE, EnumProto.SexEnum.FEMALE));
        // hashSet
        final Collection<EnumProto.SexEnum> enums3 = Sets.newHashSet(EnumProto.SexEnum.MALE, EnumProto.SexEnum.FEMALE);
        // linkedSet
        final Collection<EnumProto.SexEnum> enums4 = org.assertj.core.util.Sets.newLinkedHashSet(EnumProto.SexEnum.MALE, EnumProto.SexEnum.FEMALE);
        // treeSet
        final Collection<EnumProto.SexEnum> enums5 = org.assertj.core.util.Sets.newTreeSet(EnumProto.SexEnum.MALE, EnumProto.SexEnum.FEMALE);

        final List scenarios = Arrays.asList(enums1, enums2, enums3, enums4, enums5);
        scenarios.stream().flatMap(i -> scenarios.stream().peek(j -> {
            log.info("testEnumCollection {} Equals {}", i.getClass().getName(), j.getClass().getName());
            new Verify(i).ignoreClassDiff().asserts(j);
        })).collect(Collectors.toList());
    }

    @Test
    @DisplayName("对象Map")
    public void testObjectMap() {
        final UserInfo userInfo = Mock.run(UserInfo.class);

        // hashMap
        final HashMap<String, UserInfo> map1 = new HashMap<>();
        map1.put("1", userInfo);
        map1.put("2", userInfo.copy());

        // treeMap
        final TreeMap<String, UserInfo> map2 = new TreeMap<>();
        map2.put("2", userInfo);
        map2.put("1", userInfo.copy());

        final List scenarios = Arrays.asList(map1, map2);
        scenarios.stream().flatMap(i -> scenarios.stream().peek(j -> {
            log.info("testObjectMap {} Equals {}", i.getClass().getName(), j.getClass().getName());
            new Verify(i).ignoreClassDiff().asserts(j);
        })).collect(Collectors.toList());
    }

    @Test
    @DisplayName("对象List | 忽略字段")
    public void testObjectListIgnoreField() {
        final UserInfo userInfo = Mock.run(UserInfo.class);

        final List<UserInfo> list1 = Arrays.asList(userInfo);

        final List<UserInfo> list2 = Collections.singletonList(userInfo.toBuilder()
                .email(Mock.run(String.class, new MockConfig().setStringEnum(MockStringEnum.EMAIL)))
                .build());

        new Verify(list1).ignoredFields("email").ignoreClassDiff().asserts(list2);
    }

    @Test
    @DisplayName("对象Map | 忽略字段")
    public void testObjectMapIgnoreField() {
        final UserInfo userInfo = Mock.run(UserInfo.class);

        final HashMap<String, UserInfo> map1 = new HashMap<>();
        map1.put("1", userInfo);

        final HashMap<String, UserInfo> map2 = new HashMap<>();
        map2.put("1", userInfo.toBuilder().email(Mock.run(String.class, new MockConfig().setStringEnum(MockStringEnum.EMAIL))).build());

        new Verify(map1).ignoredFields("email").asserts(map2);
    }
}
