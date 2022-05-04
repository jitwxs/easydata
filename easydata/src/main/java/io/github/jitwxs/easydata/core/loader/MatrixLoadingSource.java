package io.github.jitwxs.easydata.core.loader;

import io.github.jitwxs.easydata.common.bean.MatrixBean;
import io.github.jitwxs.easydata.common.enums.DataTypeEnum;
import io.github.jitwxs.easydata.common.util.LineHumpUtils;
import io.github.jitwxs.easydata.common.util.ObjectUtils;
import com.google.common.collect.BiMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-26 12:04
 */
public class MatrixLoadingSource extends LoadingSource<MatrixBean> {
    public MatrixLoadingSource(LoaderProperties properties, MatrixBean source) {
        super(DataTypeEnum.MATRIX, properties, source);
    }

    @Override
    public <K> List<K> toBean(Class<K> target, BiMap<String, String> extraFiledMap) {
        if (MatrixBean.ignore(source)) {
            return Collections.emptyList();
        }

        final BiMap<String, String> extraFiledMap2 = extraFiledMap.inverse();

        final List<K> results = new ArrayList<>();

        for (String[] lines : source.getDataList()) {
            final K one = ObjectUtils.create(target);

            for (int i = 0; i < lines.length; i++) {
                final String title = source.getTitle(i), value = lines[i];

                fillingField(one, value, new String[]{LineHumpUtils.lineToHump(title), extraFiledMap.get(title), extraFiledMap2.get(title)});
            }

            results.add(one);
        }

        return results;
    }
}
