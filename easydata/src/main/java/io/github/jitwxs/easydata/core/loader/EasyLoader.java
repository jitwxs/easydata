package io.github.jitwxs.easydata.core.loader;

import io.github.jitwxs.easydata.conn.FileConnection;
import io.github.jitwxs.easydata.conn.IConnection;
import lombok.NonNull;

import java.util.Collections;
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

    /**
     * 调用加载主方法
     * @param types 加载数据类型
     * @param properties 加载参数
     * @return 加载结果
     * @param <T> 加载数据类型
     */
    public <T> List<T> loading(@NonNull final Class<T> types, @NonNull final LoaderProperties properties) {
        final Optional<LoadingSource<?>> data = this.connection.loading(properties);

        return data.map(loadingSource -> loadingSource.toBean(types, properties)).orElse(Collections.emptyList());
    }
}
