package io.github.jitwxs.easydata.core.mock.mocker.explicit;

import io.github.jitwxs.easydata.common.bean.MockConfig;
import io.github.jitwxs.easydata.common.util.TimeUtils;
import io.github.jitwxs.easydata.core.mock.EasyMock;
import io.github.jitwxs.easydata.core.mock.mocker.IMocker;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-20 16:48
 */
public class LocalDateTimeMocker implements IMocker<LocalDateTime> {
    @Override
    public LocalDateTime mock(MockConfig mockConfig) {
        final Date date = EasyMock.run(Date.class, mockConfig);

        return date == null ? null : TimeUtils.dateToLdt(date);
    }
}
