package io.github.jitwxs.easydata.conn;

import io.github.jitwxs.easydata.core.loader.JsonLoadingSource;
import io.github.jitwxs.easydata.core.loader.LoadingSource;
import io.github.jitwxs.easydata.common.bean.MatrixBean;
import io.github.jitwxs.easydata.core.loader.MatrixLoadingSource;
import io.github.jitwxs.easydata.common.enums.FileFormatEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataLoaderException;
import io.github.jitwxs.easydata.common.util.LoadingUtils;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:36
 */
@Slf4j
public class FileConnection implements IConnection {
    @Override
    public Optional<LoadingSource<?>> loading(LoaderProperties properties) {
        try {
            final String queryUrl = properties.getUrl();
            final FileFormatEnum fileFormatEnum = FileFormatEnum.delegate(queryUrl);

            switch (fileFormatEnum) {
                case CSV:
                    final MatrixBean matrixBean = MatrixBean.newInstance(LoadingUtils.loadCsv(queryUrl));
                    return Optional.of(new MatrixLoadingSource(properties, matrixBean));
                case JSON:
                    final List<String> json = LoadingUtils.loadJson(queryUrl, Charset.defaultCharset());
                    return Optional.of(new JsonLoadingSource(properties, json));
                default:
                    throw new EasyDataLoaderException("Not Support File Format To Load: " + queryUrl);
            }
        } catch (Exception e) {
            log.error("FileConnection loading error, properties: {}", properties, e);
            return Optional.empty();
        }
    }
}
