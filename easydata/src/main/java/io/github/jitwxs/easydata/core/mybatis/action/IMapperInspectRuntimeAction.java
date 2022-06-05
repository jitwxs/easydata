package io.github.jitwxs.easydata.core.mybatis.action;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;

import java.lang.reflect.Method;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-05 0:44
 */
public interface IMapperInspectRuntimeAction extends IMapperInspectAction {
    void doAction(final XMLMapperBuilder xmlMapperBuilder, final Method method, final Object[] arguments);
}
