package com.github.jitwxs.addax.core.loader;

import com.github.jitwxs.addax.common.bean.MatrixBean;
import com.github.jitwxs.addax.common.enums.DataTypeEnum;
import com.github.jitwxs.addax.common.exception.AddaxLoaderException;
import com.github.jitwxs.addax.common.util.DataConvertUtils;
import com.github.jitwxs.addax.conn.IConnection;
import com.github.jitwxs.addax.provider.LoaderPropertiesProvider;
import com.github.jitwxs.addax.provider.ProviderFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:25
 */
public class Loader {
    public final IConnection connection;

    public Loader(final IConnection connection) {
        this.connection = connection;
    }

    public <T> List<T> loading(final Class<T> types) {
        final LoaderPropertiesProvider provider = ProviderFactory.delegate(LoaderPropertiesProvider.class);

        final LoaderProperties properties = provider.delegate(types);

        final Optional<Pair<DataTypeEnum, List<String[]>>> data = this.connection.loading(properties);
        if (!data.isPresent()) {
            return null;
        }

        final DataTypeEnum dataTypeEnum = data.get().getLeft();
        final List<String[]> dataList = data.get().getRight();
        final Collection<Pair<String, String>> extraFieldMappings = emptyIfNull(properties.extraFieldMappings());

        switch (dataTypeEnum) {
            case MATRIX:
                return DataConvertUtils.matrixToList(new MatrixBean(dataList), types, extraFieldMappings);
            case JSON:
                return DataConvertUtils.jsonToList(dataList, types, extraFieldMappings);
            default:
                throw new AddaxLoaderException(String.format("Not Support Loader %s Data", dataTypeEnum));
        }
    }
}
