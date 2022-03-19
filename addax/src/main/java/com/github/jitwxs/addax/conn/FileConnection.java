package com.github.jitwxs.addax.conn;

import com.github.jitwxs.addax.core.loader.LoaderProperties;
import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import com.github.jitwxs.addax.common.enums.FileFormatEnum;
import com.github.jitwxs.addax.common.exception.AddaxLoaderException;
import com.github.jitwxs.addax.common.util.LoadingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 17:36
 */
@Slf4j
public class FileConnection implements IConnection {
    @Override
    public Optional<Pair<DataTypeEnum, List<String[]>>> loading(LoaderProperties properties) {
        try {
            final String queryUrl = properties.url().getFileConn();
            final FileFormatEnum fileFormatEnum = FileFormatEnum.delegate(queryUrl);

            final List<String[]> dataList;
            switch (fileFormatEnum) {
                case CSV:
                    dataList = LoadingUtils.loadCsv(queryUrl);
                    break;
                case JSON:
                    final List<String> jsonList = LoadingUtils.loadJson(queryUrl, Charset.defaultCharset());
                    dataList = jsonList.stream().map(e -> new String[]{e}).collect(Collectors.toList());
                    break;
                default:
                    throw new AddaxLoaderException("Not Support File Format To Load: " + queryUrl);
            }

            return Optional.of(Pair.of(DataTypeEnum.delegate(fileFormatEnum), dataList));
        } catch (Exception e) {
            log.error("FileConnection loading error, properties: {}", properties, e);
            return Optional.empty();
        }
    }
}
