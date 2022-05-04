package io.github.jitwxs.easydata.provider;

import io.github.jitwxs.easydata.common.enums.MockStringEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataMockException;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import io.github.jitwxs.easydata.core.mock.strings.IStringGenerator;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
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
    protected List<Object> uniqueKeyByInstance(IStringGenerator instance) {
        return Collections.singletonList(instance.type());
    }

    @Override
    protected Object uniqueKey(MockStringEnum... args) {
        if (args == null || args.length != 1) {
            throw new EasyDataMockException("Illegal uniqueKeyByInstance() params");
        }

        return args[0];
    }
}
