package io.github.jitwxs.easydata.conn;

import com.opencsv.CSVParser;
import io.github.jitwxs.easydata.common.bean.MatrixBean;
import io.github.jitwxs.easydata.common.enums.FileFormatEnum;
import io.github.jitwxs.easydata.common.exception.EasyDataLoaderException;
import io.github.jitwxs.easydata.common.util.LoadingUtils;
import io.github.jitwxs.easydata.core.loader.JsonLoadingSource;
import io.github.jitwxs.easydata.core.loader.LoaderProperties;
import io.github.jitwxs.easydata.core.loader.LoadingSource;
import io.github.jitwxs.easydata.core.loader.MatrixLoadingSource;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-05-18 21:21
 */
@Slf4j
public class EmbeddedConnection implements IConnection {
    private final FileFormatEnum fileFormatEnum;

    public EmbeddedConnection(final FileFormatEnum fileFormatEnum) {
        this.fileFormatEnum = fileFormatEnum;
    }

    @Override
    public Optional<LoadingSource<?>> loading(LoaderProperties properties) {
        try {
            String content = properties.getUrl();

            switch (fileFormatEnum) {
                case CSV:
                    final List<String[]> lines = new ArrayList<>();

                    final CSVParser parser = new CSVParser();

                    content = content.replaceAll("\r\n", "\n");

                    for (String line : content.split("\n")) {
                        lines.add(parser.parseLine(line));
                    }

                    final MatrixBean matrixBean = MatrixBean.newInstance(lines);
                    return Optional.of(new MatrixLoadingSource(properties, matrixBean));
                case JSON:
                    final List<String> json = LoadingUtils.loadJsonFromContent(content);
                    return Optional.of(new JsonLoadingSource(properties, json));
                default:
                    throw new EasyDataLoaderException("Not Support File Format To Load");
            }
        } catch (Exception e) {
            log.error("FileConnection loading error, properties: {}", properties, e);
            return Optional.empty();
        }
    }
}
