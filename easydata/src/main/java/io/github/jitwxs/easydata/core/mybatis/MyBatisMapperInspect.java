package io.github.jitwxs.easydata.core.mybatis;

import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.common.util.ReflectionUtils;
import io.github.jitwxs.easydata.core.mybatis.action.IMapperInspectAction;
import io.github.jitwxs.easydata.core.mybatis.action.IMapperInspectRuntimeAction;
import io.github.jitwxs.easydata.core.mybatis.action.IMapperInspectStaticAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.mockito.Mockito;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.github.jitwxs.easydata.common.util.CollectionUtils.randomElement;

/**
 * inspect for mybatis mapper file
 *
 * @author jitwxs@foxmail.com
 * @since 2022-06-04 23:27
 */
@Slf4j
public abstract class MyBatisMapperInspect<T> {
    /**
     * the mybatis mapper object
     */
    public final T target;

    /**
     * the mybatis mapper class
     */
    private final Class<T> targetClass;

    private final XMLMapperBuilder xmlMapperBuilder;

    private final List<IMapperInspectRuntimeAction> runtimeActions = new ArrayList<>();

    public abstract List<IMapperInspectAction> actionList();

    public MyBatisMapperInspect() {
        // initial targetClass
        this.targetClass = (Class<T>) ReflectionUtils.getGenericSuperClass(this.getClass())[0];
        if (!this.targetClass.isInterface()) {
            throw new EasyDataException("MyBatis mapper T must be interface");
        }

        // initial target
        this.target = Mockito.mock(this.targetClass, invocation -> {
            this.interceptorInvoke(invocation.getMethod(), invocation.getArguments());
            return null;
        });

        // initial xmlMapperBuilder
        final String xmlLocation = xmlLocation();
        try (final InputStream is = targetClass.getResourceAsStream(xmlLocation)) {
            final Configuration configuration = new Configuration();

            this.xmlMapperBuilder = new XMLMapperBuilder(is, configuration, xmlLocation, configuration.getSqlFragments());
            this.xmlMapperBuilder.parse();
        } catch (IOException e) {
            throw new EasyDataException(e);
        }

        // initial runtimeActions
        for (IMapperInspectAction action : CollectionUtils.emptyIfNull(actionList())) {
            if (action instanceof IMapperInspectStaticAction) {
                ((IMapperInspectStaticAction) action).doAction(xmlMapperBuilder, targetClass);
            } else if (action instanceof IMapperInspectRuntimeAction) {
                runtimeActions.add((IMapperInspectRuntimeAction) action);
            }
        }
    }

    /**
     * return the mapper xml file location in system, the default implements will find same name file in classpath,
     * when find nothing, will throw {@link EasyDataException}; when find more than one, will return random one.
     * you can override this method, to specify actual implements.
     *
     * @return mapper xml file location
     * @throws EasyDataException not fond xml file in classpath
     */
    public String xmlLocation() {
        final Reflections reflections = new Reflections(Scanners.Resources);

        final Set<String> resources = reflections.getResources(String.format(".*%s\\.xml", targetClass.getSimpleName()));

        if (CollectionUtils.isEmpty(resources)) {
            throw new EasyDataException("MyBatis mapper xml file location not exist");
        }

        final String location = randomElement(resources);

        return "/" + location;
    }

    /**
     * do interceptor when invoke mapper's method
     *
     * @param method    invoked method
     * @param arguments invoked method arguments
     */
    private void interceptorInvoke(final Method method, final Object[] arguments) {
        for (IMapperInspectRuntimeAction action : runtimeActions) {
            action.doAction(xmlMapperBuilder, method, arguments);
        }
    }
}
