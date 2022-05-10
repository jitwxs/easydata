package io.github.jitwxs.easydata.core.loader;

import io.github.jitwxs.easydata.conn.FileConnection;
import io.github.jitwxs.easydata.conn.IConnection;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * 数据加载主类
 *
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:25
 */
public class EasyLoader {
    public static final EasyLoader FILE_LOADER = new EasyLoader(new FileConnection());

    public final IConnection connection;

    public EasyLoader(final IConnection connection) {
        this.connection = connection;
    }

    public <T> List<T> loading(@NonNull final Class<T> types, @NonNull final LoaderProperties properties) {
        final Optional<LoadingSource<?>> data = this.connection.loading(properties);

        final BiMap<String, String> extraFiledMap = this.initialExtraFields(properties);

        return data.map(loadingSource -> loadingSource.toBean(types, extraFiledMap)).orElse(null);
    }

    private BiMap<String, String> initialExtraFields(final LoaderProperties properties) {
        final BiMap<String, String> extraFiledMap = HashBiMap.create();

        final String[] fields = properties.getExtraFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i += 2) {
                extraFiledMap.put(fields[i], fields[i + 1]);
            }
        }

        return extraFiledMap;
    }
}
