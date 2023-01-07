package io.github.jitwxs.easydata.sample.mapper;

import io.github.jitwxs.easydata.common.util.ReflectionUtils;
import io.github.jitwxs.easydata.core.mybatis.action.IMapperInspectStaticAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author jitwxs@foxmail.com
 * @since 2023-01-07 11:34
 */
@Slf4j
public class MethodParamNamesMapperInspectAction implements IMapperInspectStaticAction {
    @Override
    public void doAction(XMLMapperBuilder xmlMapperBuilder, final Class<?> mapperClass) {
        final Method[] methods = mapperClass.getDeclaredMethods();

        for (Method method : methods) {
            final String[] params = ReflectionUtils.listMethodParamNames(method);
            log.info("class: {}, method: {}, params: {}", mapperClass, method.getName(), Arrays.toString(params));
        }
    }
}
