package io.github.jitwxs.easydata.core.mock.mocker.explicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 16:48
 */
public class LocalDateMocker implements IMocker<LocalDate> {
    @Override
    public LocalDate mock(MockConfig mockConfig) {
        final LocalDateTime ldt = EasyMock.run(LocalDateTime.class);

        return ldt == null ? null : ldt.toLocalDate();
    }
}
