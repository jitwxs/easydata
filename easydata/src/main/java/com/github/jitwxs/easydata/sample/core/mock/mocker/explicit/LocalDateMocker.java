package com.github.jitwxs.easydata.sample.core.mock.mocker.explicit;

import com.github.jitwxs.easydata.sample.common.bean.MockConfig;
import com.github.jitwxs.easydata.sample.core.mock.EasyMock;
import com.github.jitwxs.easydata.sample.core.mock.mocker.IMocker;

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
