package io.github.jitwxs.easydata.core.mybatis.action;

import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import lombok.AllArgsConstructor;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * inspect mybatis mapper file resultMap with columns sql is match
 *
 * @author jitwxs@foxmail.com
 * @since 2022-06-05 11:03
 */
@AllArgsConstructor
public class ColumnsMapperInspectAction implements IMapperInspectStaticAction {
    /**
     * mybatis mapper xml columns sql id
     */
    private final String columnsId;

    /**
     * mybatis mapper xml mapping resultMap's id
     */
    private final String resultMapId;

    @Override
    public void doAction(XMLMapperBuilder xmlMapperBuilder, final Class<?> mapperClass) {
        final Configuration configuration = xmlMapperBuilder.getConfiguration();

        final ResultMap resultMap = configuration.getResultMap(resultMapId);
        if (resultMap == null) {
            throw new EasyDataException("Not found resultMap: " + resultMapId);
        }

        final XNode columnsNode = configuration.getSqlFragments().getOrDefault(columnsId, null);
        if (columnsNode == null) {
            throw new EasyDataException("Not found columns sql: " + columnsId);
        }

        final Set<String> actual = Arrays.stream(columnsNode.getStringBody().replaceAll("`", "").trim().split(","))
                .map(String::trim).collect(Collectors.toSet());

        final Set<String> except = resultMap.getResultMappings().stream().map(ResultMapping::getColumn).collect(Collectors.toSet());

        EasyVerify.with(except, actual).verify();
    }
}
