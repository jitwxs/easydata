package io.github.jitwxs.easydata.core.mybatis.action;

import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.common.exception.EasyDataVerifyException;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-06-05 11:11
 */
@Slf4j
public class SqlStatementMapperInspectAction implements IMapperInspectRuntimeAction {
    @Override
    public void doAction(XMLMapperBuilder xmlMapperBuilder, Method method, Object[] arguments) {
        final Configuration configuration = xmlMapperBuilder.getConfiguration();

        final MappedStatement statement = configuration.getMappedStatement(method.getName());
        if (statement == null) {
            throw new EasyDataException("Not found mappedStatement: " + method.getName());
        }

        final String sql = this.compileSql(method, arguments, statement);

        try {
            log.info("SqlStatementMapperInspectAction check: {}", sql);
            // if execute not throws exception, instructions sql correct
            new MySqlStatementParser(sql).parseStatementList();
        } catch (Exception e) {
            throw new EasyDataVerifyException("Sql statement inspect failed: " + e.getMessage());
        }
    }

    private String compileSql(final Method method, final Object[] arguments, final MappedStatement statement) {
        final Parameter[] parameters = method.getParameters();
        final Annotation[][] annotations = method.getParameterAnnotations();

        if (parameters.length != arguments.length || parameters.length != annotations.length) {
            throw new EasyDataVerifyException("Parameter or annotation length not equals");
        }

        // prepare sql params map, process @param annotation
        final Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            final Object argument = arguments[i];

            String key = null;
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof Param) {
                    key = ((Param) annotation).value();
                    break;
                }
            }
            if (key == null) {
                key = parameters[i].getName();
            }

            paramMap.put(key, argument);
        }

        // prepare sql statement, replace placeholder
        final BoundSql boundSql = statement.getBoundSql(paramMap);
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for (char c : boundSql.getSql().toCharArray()) {
            if (c == '?') {
                final String property = boundSql.getParameterMappings().get(index).getProperty();

                Object parameter = null;

                final Object parameterObject = boundSql.getParameterObject();
                if (parameterObject instanceof Map) {
                    parameter = ((Map) parameterObject).get(property);
                }

                if (parameter == null) {
                    parameter = boundSql.getAdditionalParameter(property);
                }

                if (parameter == null) {
                    Map<String, Object> params = (Map<String, Object>) boundSql.getAdditionalParameter("_paramter");
                    if (params != null) {
                        parameter = params.get(property);
                    }
                }

                EasyVerify.notNull(parameter, () -> "mapper parameter [" + property + "] not allow null");

                sb.append("'").append(parameter).append("'");
                index++;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
