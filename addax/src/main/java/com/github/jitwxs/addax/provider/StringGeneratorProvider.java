package com.github.jitwxs.addax.provider;

import com.github.jitwxs.addax.common.enums.MockStringEnum;
import com.github.jitwxs.addax.common.exception.AddaxMockException;
import com.github.jitwxs.addax.common.util.ObjectUtils;
import com.github.jitwxs.addax.core.mock.strings.IStringGenerator;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 20:18
 */
public class StringGeneratorProvider extends Provider<IStringGenerator, MockStringEnum> {
    @Override
    protected List<IStringGenerator> loadNative() {
        final List<IStringGenerator> resultList = new ArrayList<>();

        new Reflections(IStringGenerator.class.getPackage().getName()).getSubTypesOf(IStringGenerator.class).forEach(c -> {
            if (Modifier.isInterface(c.getModifiers())) {
                return;
            }
            resultList.add(ObjectUtils.create(c));
        });

        return resultList;
    }

    @Override
    protected Object uniqueKeyByInstance(IStringGenerator instance) {
        return instance.type();
    }

    @Override
    protected Object uniqueKey(MockStringEnum... args) {
        if (args == null || args.length != 1) {
            throw new AddaxMockException("Illegal uniqueKeyByInstance() params");
        }

        return args[0];
    }
}
