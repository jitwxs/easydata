package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.core.loader.JsonLoadingSource;
import com.github.jitwxs.addax.core.loader.LoadingSource;
import com.github.jitwxs.addax.common.bean.MatrixBean;
import com.github.jitwxs.addax.core.loader.MatrixLoadingSource;
import com.github.jitwxs.addax.common.enums.FileFormatEnum;
import com.github.jitwxs.addax.common.exception.AddaxLoaderException;
import com.github.jitwxs.addax.common.util.LoadingUtils;
import com.github.jitwxs.addax.core.loader.LoaderProperties;
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
            final String queryUrl = properties.getFileUrl();
            final FileFormatEnum fileFormatEnum = FileFormatEnum.delegate(queryUrl);

            switch (fileFormatEnum) {
                case CSV:
                    final MatrixBean matrixBean = new MatrixBean(LoadingUtils.loadCsv(queryUrl));
                    return Optional.of(new MatrixLoadingSource(properties, matrixBean));
                case JSON:
                    final List<String> json = LoadingUtils.loadJson(queryUrl, Charset.defaultCharset());
                    return Optional.of(new JsonLoadingSource(properties, json));
                default:
                    throw new AddaxLoaderException("Not Support File Format To Load: " + queryUrl);
            }
        } catch (Exception e) {
            log.error("FileConnection loading error, properties: {}", properties, e);
            return Optional.empty();
        }
    }
}
