package io.github.jitwxs.easydata.core.mybatis.action;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-05 0:45
 */
public interface IMapperInspectStaticAction extends IMapperInspectAction {
    void doAction(final XMLMapperBuilder xmlMapperBuilder);
}
