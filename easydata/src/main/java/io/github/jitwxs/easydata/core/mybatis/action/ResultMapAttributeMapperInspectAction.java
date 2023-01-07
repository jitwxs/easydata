package io.github.jitwxs.easydata.core.mybatis.action;

import io.github.jitwxs.easydata.common.cache.PropertyCache;
import io.github.jitwxs.easydata.common.enums.ClassDiffVerifyStrategyEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataException;
import io.github.jitwxs.easydata.core.verify.EasyVerify;
import io.github.jitwxs.easydata.core.verify.VerifyInstance;
import lombok.AllArgsConstructor;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;

import java.util.Set;

/**
 * inspect mybatis mapper file resultMap with entity attribute is match
 *
 * @author jitwxs@foxmail.com
 * @since 2022-06-05 9:39
 */
@AllArgsConstructor
public class ResultMapAttributeMapperInspectAction implements IMapperInspectStaticAction {
    /**
     * mybatis mapper xml inspect resultMap's id
     */
    private final String resultMapId;

    /**
     * ignore inspect field in resultMap
     */
    private final String[] ignoreFields;

    @Override
    public void doAction(XMLMapperBuilder xmlMapperBuilder, final Class<?> mapperClass) {
        final Configuration configuration = xmlMapperBuilder.getConfiguration();

        final ResultMap resultMap = configuration.getResultMap(resultMapId);
        if (resultMap == null) {
            throw new EasyDataException("Not found resultMap: " + resultMapId);
        }

        final Class<?> target = resultMap.getType();

        final Set<String> except = PropertyCache.tryGet(target).getAll().keySet();

        final Set<String> actual = resultMap.getMappedProperties();

        final VerifyInstance instance = EasyVerify.with(except, actual);
        if (ignoreFields != null && ignoreFields.length > 0) {
            // using ignore elements
            instance.ignoreElements(ignoreFields);
        }
        instance.ignoreClassDiff(ClassDiffVerifyStrategyEnum.CONVERT_SAME_CLASS).verify();
    }
}
